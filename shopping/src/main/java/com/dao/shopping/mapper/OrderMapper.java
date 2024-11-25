package com.dao.shopping.mapper;

import com.dao.shopping.dto.responses.CartItemResponse;
import com.dao.shopping.dto.responses.OrderResponse;
import com.dao.shopping.entity.OrderEntity;


import java.util.List;
import java.util.stream.Collectors;

public class OrderMapper {

    public static OrderResponse toOrderDTOResponse(OrderEntity orderEntity) {

        List<CartItemResponse> responseList = orderEntity.getOrderItems().stream()
                .map(orderItem ->
                        CartItemResponse.builder()
                                .cartItemId(orderItem.getCartItemId())
                                .productId(orderItem.getVariant().getProduct().getId())
                                .productImage(orderItem.getVariant().getProduct().getImage())
                                .productName(orderItem.getVariant().getProduct().getName())
                                .variant(ProductVariantMapper.toProductVariantResponse(orderItem.getVariant()))
                                .productQuantity(orderItem.getQuantity())
                                .productPrice(orderItem.getPrice())
                                .build())
                .collect(Collectors.toList());

        return OrderResponse.builder()
                .orderId(orderEntity.getId())
                .productList(responseList)
                .totalPrice(orderEntity.getTotalPrice())
                .createdDate(orderEntity.getCreatedDate())
                .createdBy(orderEntity.getCreatedBy())
                .lastModifiedBy(orderEntity.getLastModifiedBy())
                .lastModifiedDate(orderEntity.getLastModifiedDate())
                .orderStatus(String.valueOf(orderEntity.getOrderStatus()))
                .shippingCost(orderEntity.getShippingCost())
                .totalQuantity(orderEntity.getTotalQuantity())
                .recipient(RecipientMapper.toRecipientResponse(orderEntity.getRecipient()))
                .deliveryAddress(AddressMapper.mapAddressToAddressResponse(orderEntity.getAddress()))
                .voucher(VoucherMapper.mapVoucherToVoucherResponse(orderEntity.getVoucher()))
                .build();
    }



}
