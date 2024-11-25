package com.dao.shopping.service.impl;


import com.dao.shopping.dto.requests.*;
import com.dao.shopping.dto.responses.OrderResponse;
import com.dao.shopping.dto.responses.OrderSummaryResponse;
import com.dao.shopping.entity.*;
import com.dao.shopping.enums.PaymentType;
import com.dao.shopping.exception.CustomExceptionHandler;
import com.dao.shopping.mapper.OrderMapper;
import com.dao.shopping.repository.*;
import com.dao.shopping.repository.criteria.RecipientAddressCriteria;
import com.dao.shopping.service.IOrderService;
import com.dao.shopping.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements IOrderService {
    private final OrderRepository orderRepository;
    private final IUserService iUserService;
    private final CartRepository cartRepository;
    private final AddressRepository addressRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductVariantRepository variantRepository;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final PaymentRepository paymentRepository;
    private final VoucherRepository voucherRepository;
    private final RecipientRepository recipientRepository;
    private final RecipientAddressCriteria recipientAddressCriteria;

    @Override
    public OrderSummaryResponse orderItem(OrderItemRequest request) {

        UserEntity userEntityLoggedIn = iUserService.getUserLoggedIn();

        if (userEntityLoggedIn == null) {
            throw CustomExceptionHandler.unauthorizedException("User must be logged in. Please login before ordering any products");
        }

        AddressRequest addressRequest = request.getAddress();

        RecipientRequest recipientRequest = request.getRecipient();

        List<Integer> cartItemIds = request.getCartItemIds();

        if (CollectionUtils.isEmpty(cartItemIds)) {
            throw CustomExceptionHandler.badRequestException("At least one cart item is required");
        }

        PaymentRequest paymentRequest = request.getPayment();

        CartEntity cartEntity = userEntityLoggedIn.getCart();

        List<CartItemEntity> cartItemEntityList = cartEntity.getCartItems();

        List<OrderItemEntity> orderItemEntities = request.getCartItemIds().stream()
                .map(cartItemRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(cartItem -> {
                    if (!cartItemEntityList.contains(cartItem)) {
                        throw CustomExceptionHandler.badRequestException("One or more cart items do not belong to the user");
                    }
                    return true;
                })
                .map(cartItem -> {

                    ProductVariantEntity variant = cartItem.getVariant();
                    variant.setSoldQuantity(variant.getSoldQuantity() + cartItem.getQuantity());

                    ProductEntity productEntity = variant.getProduct();
                    int totalSoldQuantity = productEntity.getVariants().stream()
                            .mapToInt(ProductVariantEntity::getSoldQuantity)
                            .sum();
                    productEntity.setTotalSoldQuantity(totalSoldQuantity);
                    productRepository.save(productEntity);

                    if (variant.getSoldQuantity() == 0) {
                        variant.setAvailable(false);
                        variant.setSoldOut(true);
                        variantRepository.save(variant);
                    }

                    cartItemEntityList.remove(cartItem);
                    cartEntity.getCartItems().remove(cartItem);
                    cartItemRepository.save(cartItem);
                    cartRepository.save(cartEntity);

                    return mapCartItemToOrderItem(cartItem);
                })
                .toList();

        //process with address request and recipient request
        AddressEntity addressEntity = (addressRequest != null) ?
                getOrCreateAddress(addressRequest, userEntityLoggedIn) :
                getFirstAddress(userEntityLoggedIn.getId());

        RecipientEntity recipientEntity = (recipientRequest != null) ?
                getOrCreateRecipient(recipientRequest, addressEntity, userEntityLoggedIn) :
                getFirstRecipient(userEntityLoggedIn.getId(), addressEntity.getId());

        int totalQuantity = orderItemEntities.stream().mapToInt(OrderItemEntity::getQuantity).sum();

        BigDecimal totalPrice = orderItemEntities.stream()
                .map(orderItemEntity -> orderItemEntity.getPrice().multiply(BigDecimal.valueOf(orderItemEntity.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setUser(userEntityLoggedIn);
        //default order status is pending
        orderEntity.setUpdateStatusDate(new Date());
        orderEntity.setOrderItems(orderItemEntities);
        orderEntity.setTotalQuantity(totalQuantity);
        orderEntity.setTotalPrice(totalPrice);

        BigDecimal finalPrice = BigDecimal.ZERO;

        //free shipping for orders have the city address is Ha Noi capital
        if (isFreeShippingForHanoi(orderEntity)) {
            orderEntity.setShippingCost(BigDecimal.ZERO);
        }

        //chua xu ly voi final price
        orderEntity.setCreatedDate(LocalDateTime.now());
        orderEntity.setAddress(addressEntity);
        orderEntity.setRecipient(recipientEntity);

        //apply voucher for order and process with final price
        List<VoucherEntity> availableVouchers = voucherRepository.findAll();

        //Just the city address is Ha Noi all have been freely shipping
        //totalPrice >= minOrderValue (10tr VND) => discount 2% if city's Ha Noi => free shipping
        //30tr => discount 3% => free shipping
        //50tr => discount 5% => free shipping;
        //100tr => discount 7% => free shipping
        //Commonly speaking those bills have a total price greater than equal 30tr VN => free shipping
        //and will be attached with any gifts that we provide for voucher purposes
        VoucherEntity applyVoucher = availableVouchers.stream()
                .filter(voucher -> {
                    //condition 1: free shipping for those orders have total price greater than equal 30M VND
                    //and apply voucher is attached
                    if (orderEntity.getTotalPrice().compareTo(voucher.getMinOrderValue()) >= 0 && isEligibleForFreeShipping(orderEntity)) {
                        orderEntity.setShippingCost(BigDecimal.ZERO);
                        orderEntity.setDiscountPrice(calculateDiscountForOrder(orderEntity, voucher));
                        orderEntity.setAppliedVoucher(voucher.getCode());
                        orderEntity.setFinalPrice(calculateFinalPriceForOrder(orderEntity));
                        return true;
                    }
                    return false;
                })
                .findFirst()
                .orElse(null);

        if (applyVoucher != null) {
            orderEntity.setVoucher(applyVoucher);
            finalPrice = orderEntity.getTotalPrice().add(orderEntity.getShippingCost()).subtract(orderEntity.getDiscountPrice());
            orderEntity.setFinalPrice(finalPrice);
        }

        PaymentEntity paymentEntity = paymentForOrder(paymentRequest, recipientEntity, userEntityLoggedIn, finalPrice);

        orderEntity.setPayment(paymentEntity);
        paymentRepository.save(paymentEntity);
        orderRepository.save(orderEntity);

        //address
        StringJoiner deliveryAddress = new StringJoiner(", ");
        deliveryAddress.add(addressEntity.getHouseNumber());
        deliveryAddress.add(addressEntity.getStreet());
        deliveryAddress.add(addressEntity.getDistrict());
        deliveryAddress.add(addressEntity.getCity());
        deliveryAddress.add(addressEntity.getCountry());

        OrderSummaryResponse summaryResponse = mapOrderToOrderSummaryResponse(orderEntity);
        summaryResponse.setDeliveryAddress(deliveryAddress.toString());
        summaryResponse.setPaymentType(paymentRequest.getPaymentMethod());
        summaryResponse.setCreateBy(userEntityLoggedIn.getCreatedBy());
        summaryResponse.setRecipientName(recipientEntity.getName());
        summaryResponse.setRecipientPhone(recipientEntity.getPhone());

        return summaryResponse;
    }

    @Override
    public OrderResponse getOrderDetailById(Integer orderId) {

        OrderEntity orderEntity = orderRepository.findById(orderId)
                .orElseThrow(() -> CustomExceptionHandler.notFoundException("Order not found"));

        return OrderMapper.toOrderDTOResponse(orderEntity);
    }

    @Override
    public OrderSummaryResponse getOrderById(Integer orderId) {
        UserEntity userEntityLoggedIn = iUserService.getUserLoggedIn();

        if (userEntityLoggedIn == null) {
            throw CustomExceptionHandler.notFoundException("User is not logged in");
        }

        List<OrderEntity> orderEntities = userEntityLoggedIn.getOrders();

        OrderEntity orderEntity = orderRepository.findById(orderId)
                .orElseThrow(() -> CustomExceptionHandler.notFoundException("Order of user isn't found"));

        if (!orderEntities.contains(orderEntity)) {
            throw CustomExceptionHandler.badRequestException("This order does not exist or don't belong to this user." +
                    " Please recheck your order");
        }

        return mapOrderToOrderSummaryResponse(orderEntity);
    }


    @Override
    public OrderResponse updateOrderStatus(Integer orderId , OrderStatusUpdate newStatus) {

        if (newStatus.getStatus() == null) {
            throw CustomExceptionHandler.badRequestException("Status is required");
        }

        OrderEntity orderEntity = orderRepository.findById(orderId)
                .orElseThrow(() -> CustomExceptionHandler.notFoundException("Order id is not found"));

        orderEntity.setOrderStatus(newStatus.getStatus());
        orderEntity.setUpdateStatusDate(new Date());

        orderRepository.save(orderEntity);

        int totalQuantity = orderEntity.getOrderItems().stream()
                .mapToInt(OrderItemEntity::getQuantity)
                .sum();

        OrderResponse response = OrderMapper.toOrderDTOResponse(orderEntity);

        response.setTotalQuantity(totalQuantity);
        response.setOrderId(orderId);
        response.setOrderStatus(String.valueOf(orderEntity.getOrderStatus()));

        return response;
    }

    @Override
    public List<OrderResponse> getAllOrdersOfAllUsers() {

        List<OrderEntity> orderEntityEntities = orderRepository.findAll();

        return orderEntityEntities.stream()
                .map(orderEntity -> {

                    OrderResponse response = OrderMapper.toOrderDTOResponse(orderEntity);

                    int totalQuantity = orderEntity.getOrderItems().stream().mapToInt(OrderItemEntity::getQuantity).sum();

                    response.setTotalQuantity(totalQuantity);

                    return response;

                }).toList();

    }

    @Override
    public List<OrderResponse> getOrderProduct() {

        UserEntity userEntityLoggedIn = iUserService.getUserLoggedIn();

        if (userEntityLoggedIn == null){
            throw CustomExceptionHandler.notFoundException("User is not logged in");
        }

        List<OrderEntity> orderEntityEntities = userEntityLoggedIn.getOrders();

        if (orderEntityEntities == null){
            throw CustomExceptionHandler.notFoundException("You don't have any orders yet");
        }

        return orderEntityEntities.stream()
                .map(orderEntity -> {

                OrderResponse response = OrderMapper.toOrderDTOResponse(orderEntity);

                int totalQuantity = orderEntity.getOrderItems().stream().mapToInt(OrderItemEntity::getQuantity).sum();

                response.setTotalQuantity(totalQuantity);

                return response;

                }).toList();
    }

    private OrderItemEntity mapCartItemToOrderItem(CartItemEntity cartItemEntity){
        return OrderItemEntity.builder()
                .cartItemId(cartItemEntity.getId())
                .price(cartItemEntity.getPrice())
                .variant(cartItemEntity.getVariant())
                .quantity(cartItemEntity.getQuantity())
                .build();
    }

    private BigDecimal calculateDiscountForOrder(OrderEntity orderEntity, VoucherEntity voucherEntity) {
        BigDecimal totalPrice = orderEntity.getTotalPrice();

        BigDecimal discountPercentage = voucherEntity.getDiscountPercentage() != null ?
                BigDecimal.valueOf(voucherEntity.getDiscountPercentage()) : BigDecimal.ZERO;

        if (orderEntity.getTotalPrice() == null) {
            throw new IllegalArgumentException("Order or total price cannot be null.");
        }

        return totalPrice.multiply(discountPercentage).divideToIntegralValue(BigDecimal.valueOf(100));
    }

    private BigDecimal calculateFinalPriceForOrder(OrderEntity orderEntity){
        BigDecimal totalPrice = orderEntity.getTotalPrice();
        BigDecimal discountPrice = calculateDiscountForOrder(orderEntity, orderEntity.getVoucher());
        BigDecimal shippingCost = orderEntity.getShippingCost();

        BigDecimal finalPrice = totalPrice.subtract(discountPrice).add(shippingCost);
        orderEntity.setFinalPrice(finalPrice);
        return finalPrice;
    }

    private boolean isEligibleForFreeShipping(OrderEntity orderEntity) {
        return (orderEntity.getTotalPrice().compareTo(BigDecimal.valueOf(30000000)) >= 0);
    }

    private boolean isFreeShippingForHanoi(OrderEntity orderEntity){
        String city = orderEntity.getAddress().getCity().trim().toLowerCase();
        return city.equals("hà nội") || city.equals("hanoi") || city.equals("ha noi") || city.equals("hn");
    }

    private PaymentEntity paymentForOrder(PaymentRequest paymentRequest, RecipientEntity newRecipientEntity, UserEntity userEntityLoggedIn,
                                          BigDecimal finalPrice){

        PaymentEntity paymentEntity = new PaymentEntity();

        PaymentType paymentType = paymentRequest.getPaymentMethod() != null ? paymentRequest.getPaymentMethod() : PaymentType.COD;
        BigDecimal amount = paymentRequest.getAmount();

        paymentEntity.setPaymentType(paymentType);
        paymentEntity.setAmount(amount);

        paymentEntity.setPayee(newRecipientEntity);
        if (amount != null){

            int comparePrice = amount.compareTo(finalPrice);

            if (comparePrice == 0 || comparePrice > 0){
                paymentEntity.setPayer(userEntityLoggedIn);
                paymentEntity.setPaymentStatus(true);
            }
            else {
                paymentEntity.setPaymentStatus(false);
            }
        }
        else paymentEntity.setPaymentStatus(false);

        return paymentEntity;
    }

    private AddressEntity getFirstAddress(Integer userId){
        return addressRepository.findFirstByUserId(userId)
                .orElseThrow(() -> CustomExceptionHandler.notFoundException("No address found for account"));
    }

    private RecipientEntity getFirstRecipient(Integer userId, Integer addressId){
        return recipientRepository.findFirstByUserIdAndAddressId(userId, addressId)
                .orElseThrow(() -> CustomExceptionHandler.notFoundException("No recipient found for account"));
    }

    private AddressEntity getOrCreateAddress(AddressRequest addressRequest, UserEntity userEntityLoggedIn){
        AddressEntity existingAddressEntity = recipientAddressCriteria.findExistingAddress(addressRequest, userEntityLoggedIn);
        AddressEntity newAddressEntity = existingAddressEntity != null ? existingAddressEntity : modelMapper.map(addressRequest, AddressEntity.class);
        addressRepository.save(newAddressEntity);
        return newAddressEntity;
    }

    private RecipientEntity getOrCreateRecipient(RecipientRequest recipientRequest, AddressEntity newAddressEntity, UserEntity userEntityLoggedIn){
        RecipientEntity existingRecipientEntity = recipientAddressCriteria.findExistingRecipient(recipientRequest, userEntityLoggedIn);
        RecipientEntity newRecipientEntity = existingRecipientEntity != null ? existingRecipientEntity : modelMapper.map(recipientRequest, RecipientEntity.class);
        newRecipientEntity.getAddresses().add(newAddressEntity);
        recipientRepository.save(newRecipientEntity);
        return newRecipientEntity;
    }

    private OrderSummaryResponse mapOrderToOrderSummaryResponse(OrderEntity orderEntity){
        OrderSummaryResponse response = new OrderSummaryResponse();
        response.setOrderId(orderEntity.getId());
        response.setTotalPrice(orderEntity.getTotalPrice());
        response.setShippingCost(orderEntity.getShippingCost());
        response.setDiscountPrice(orderEntity.getDiscountPrice());
        response.setCreatedDate(orderEntity.getCreatedDate());
        response.setFinalPrice(orderEntity.getFinalPrice());
        response.setPaymentSuccess(orderEntity.getPayment().isPaymentStatus());
        return response;
    }

}

