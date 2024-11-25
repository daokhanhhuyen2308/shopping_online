package com.dao.shopping.service.impl;

import com.dao.shopping.dto.requests.AddToCartRequest;
import com.dao.shopping.dto.requests.CartItemUpdateRequest;
import com.dao.shopping.dto.responses.CartItemResponse;
import com.dao.shopping.dto.responses.CartResponse;
import com.dao.shopping.entity.*;
import com.dao.shopping.exception.CustomExceptionHandler;
import com.dao.shopping.mapper.CartMapper;
import com.dao.shopping.repository.CartItemRepository;
import com.dao.shopping.repository.CartRepository;
import com.dao.shopping.repository.ProductRepository;
import com.dao.shopping.repository.ProductVariantRepository;
import com.dao.shopping.service.ICartService;
import com.dao.shopping.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartServiceImpl implements ICartService {
    private final IUserService iUserService;
    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final ProductVariantRepository variantRepository;

    @Override
    @Transactional
    public CartResponse addToCart(AddToCartRequest request, Integer page, Integer size) {

        Pageable pageable = PageRequest.of(page - 1, size);

        UserEntity userEntityLoggedIn = iUserService.getUserLoggedIn();

        int subQuantityProduct, subQuantityVariant;

        if (userEntityLoggedIn == null){
            throw CustomExceptionHandler.notFoundException("User is not logged in");
        }

        Optional<ProductEntity> optionalProduct = productRepository.findById(request.getProductId());
        Optional<ProductVariantEntity> variantOptional = variantRepository.findById(request.getVariant().getVariantId());

        if (optionalProduct.isEmpty()){
            throw CustomExceptionHandler.notFoundException("Product id's not found");
        }

        if (variantOptional.isEmpty()){
            throw CustomExceptionHandler.notFoundException("Product variant id's not found");
        }

        ProductEntity productEntity = optionalProduct.get();
        ProductVariantEntity variant = variantOptional.get();

        CartEntity cartEntity = userEntityLoggedIn.getCart();

        List<CartItemEntity> cartItemEntityList = cartEntity.getCartItems();

        if (cartItemEntityList == null){
            cartEntity.setCartItems(new ArrayList<>());
        }

        int quantityPurchased = request.getVariant().getQuantity() != null ? request.getVariant().getQuantity() : 1;

        if (productEntity.getTotalQuantityReceived() == 0 || productEntity.getTotalQuantityReceived() < quantityPurchased){
            throw CustomExceptionHandler.notFoundException("Product is out of stock");
        }

        if (variant.getQuantity() < quantityPurchased){
            throw CustomExceptionHandler.notFoundException("This variant is out of stock. Please choose another variant");
        }

        assert cartItemEntityList != null;

        Optional<CartItemEntity> existingCartItemOptional = cartItemEntityList.stream()
                .filter(cartItem -> Objects.equals(cartItem.getVariant().getProduct().getId(), request.getProductId())
                && Objects.equals(cartItem.getVariant().getId(), request.getVariant().getVariantId()))
                .findFirst();

        if (existingCartItemOptional.isPresent()){

            CartItemEntity existingCartItemEntity = existingCartItemOptional.get();

            int newQuantity = request.getVariant().getQuantity();
            int previousQuantity = existingCartItemEntity.getQuantity();

            existingCartItemEntity.setQuantity(newQuantity);
            existingCartItemEntity.setPrice(productEntity.getPrice().multiply(BigDecimal.valueOf(newQuantity)));

            int quantityDifference = newQuantity - previousQuantity;

            subQuantityProduct = productEntity.getTotalQuantityReceived() - quantityDifference;
            subQuantityVariant = variant.getQuantity() - quantityDifference;

            productEntity.setTotalQuantityReceived(subQuantityProduct);
            variant.setQuantity(subQuantityVariant);

            if (productEntity.getTotalQuantityReceived() == 0){
                productEntity.setAvailable(false);
            }

            if (variant.getQuantity() == 0){
                variant.setAvailable(false);
            }

            productRepository.save(productEntity);
            variantRepository.save(variant);
            cartItemRepository.save(existingCartItemEntity);

        }else {

            CartItemEntity cartItemEntity = new CartItemEntity();
            cartItemEntity.setCart(cartEntity);
            cartItemEntity.setVariant(variant);
            cartItemEntity.setQuantity(quantityPurchased);
            cartItemEntity.setDiscountedPrice(productEntity.getDiscountedPrice());
            cartItemEntity.setPrice(productEntity.getPrice().multiply(BigDecimal.valueOf(quantityPurchased)));

            productEntity.setTotalQuantityReceived(productEntity.getTotalQuantityReceived() - quantityPurchased);
            variant.setQuantity(variant.getQuantity() - quantityPurchased);

            productRepository.save(productEntity);
            variantRepository.save(variant);

            cartItemRepository.save(cartItemEntity);
            cartItemEntityList.add(cartItemEntity);
            cartEntity.setCartItems(cartItemEntityList);
            cartRepository.save(cartEntity);

        }

        if (variant.getQuantity() == 0){
            variant.setAvailable(false);
        }

        if (productEntity.getTotalQuantityReceived() == 0){
            productEntity.setAvailable(false);
        }

        productRepository.save(productEntity);
        variantRepository.save(variant);

        int totalQuantity = cartItemEntityList.stream().mapToInt(CartItemEntity::getQuantity).sum();
        cartEntity.setTotalQuantity(totalQuantity);
        cartRepository.save(cartEntity);

        return CartMapper.toCartDTOResponse(cartEntity, pageable);

    }

    @Override
    public Map<String, Object> getCartItems(Integer page, Integer size) {

        Pageable pageable = PageRequest.of(page, size);

        UserEntity userEntityLoggedIn = iUserService.getUserLoggedIn();

        if (userEntityLoggedIn == null) {
            throw CustomExceptionHandler.notFoundException("User is not logged in");
        }

        CartEntity cartEntity = userEntityLoggedIn.getCart();

        if (cartEntity == null) {

            log.info("There are no products in the cart");

            return new HashMap<>();
        }

        List<CartItemEntity> cartItemEntities = cartEntity.getCartItems();

        List<CartItemResponse> cartItemResponses = cartItemEntities.stream().map(CartMapper::toCartItemDTOResponse).toList();

        int totalQuantity = cartItemResponses.stream().mapToInt(CartItemResponse::getProductQuantity).sum();

        CartResponse cartResponse = CartMapper.toCartDTOResponse(cartEntity, pageable);

        cartResponse.setTotalQuantity(totalQuantity);

        Page<CartItemEntity> cartItemEntityPage = cartItemRepository.findByCart(cartEntity, pageable);

        Page<CartItemResponse> responsePage = cartItemEntityPage.map(CartMapper::toCartItemDTOResponse);

            Map<String, Object> response = new HashMap<>();

            response.put("cart", cartItemResponses);
            response.put("currentPage", responsePage.getNumber());
            response.put("totalPages", responsePage.getTotalPages());
            response.put("totalElements", responsePage.getTotalElements());
            response.put("size", responsePage.getSize());

            return response;
    }

    @Override
    @Transactional
    public CartItemResponse updateCartItem(Integer cartItemId, CartItemUpdateRequest update) {

        UserEntity userEntityLoggedIn = iUserService.getUserLoggedIn();

        if (userEntityLoggedIn == null) {
            throw CustomExceptionHandler.notFoundException("User is not logged in");
        }

        CartEntity cartEntity = userEntityLoggedIn.getCart();

        if (cartEntity == null) {
            throw CustomExceptionHandler.notFoundException("There are no products in the cart");
        }

        Optional<CartItemEntity> cartItemOptional = cartItemRepository.findById(cartItemId);

        if (cartItemOptional.isEmpty()) {
            throw CustomExceptionHandler.notFoundException("Cart item not found");
        }

        CartItemEntity cartItemEntity = cartItemOptional.get();

        if (!cartItemEntity.getCart().equals(cartEntity)) {
            throw CustomExceptionHandler.badRequestException("CartItem does not belong to the user's cart");
        }

        int newQuantity = update.getNewQuantity();
        int previousQuantity = cartItemEntity.getQuantity();

        ProductVariantEntity variant = cartItemEntity.getVariant();
        ProductEntity productEntity = variant.getProduct();

        int currentVariantQuantity = variant.getQuantity();
        int currentTotalQuantity = productEntity.getTotalQuantityReceived();

        int availableQuantity = currentVariantQuantity + previousQuantity;

        if (newQuantity > availableQuantity) {
            throw CustomExceptionHandler.badRequestException("Requested quantity exceeds available quantity");
        }

        int updatedVariantQuantity = currentVariantQuantity + previousQuantity - newQuantity;
        int updatedTotalQuantity = currentTotalQuantity + previousQuantity - newQuantity;

        variant.setQuantity(updatedVariantQuantity);
        productEntity.setTotalQuantityReceived(updatedTotalQuantity);

        productEntity.setAvailable(updatedTotalQuantity != 0);
        variant.setAvailable(updatedTotalQuantity != 0);

        productRepository.save(productEntity);
        variantRepository.save(variant);

        cartItemEntity.setQuantity(newQuantity);
        cartItemEntity.setPrice(productEntity.getPrice().multiply(BigDecimal.valueOf(newQuantity)));

        cartItemEntity = cartItemRepository.save(cartItemEntity);

        return CartMapper.toCartItemDTOResponse(cartItemEntity);

    }

    @Override
    @Transactional
    public synchronized CartResponse deleteCartItem(Integer cartItemId, Integer page, Integer size) {

        Pageable pageable = PageRequest.of(page - 1, size);

        UserEntity userEntityLoggedIn = iUserService.getUserLoggedIn();
        if (userEntityLoggedIn == null){
            throw CustomExceptionHandler.notFoundException("User is not logged in");
        }

        CartEntity cartEntity = userEntityLoggedIn.getCart();

        Optional<CartItemEntity> cartItemOptional = cartItemRepository.findById(cartItemId);

        if (cartItemOptional.isEmpty()) {
            throw CustomExceptionHandler.notFoundException("Cart item is not found");
        }

        CartItemEntity cartItemEntity = cartItemOptional.get();

        if (!cartItemEntity.getCart().equals(cartEntity)) {
            throw CustomExceptionHandler.badRequestException("Cart does not belong to the user's cart");
        }

        int quantityToRestore = cartItemEntity.getQuantity();

        ProductEntity productEntity = cartItemEntity.getVariant().getProduct();
        ProductVariantEntity variant = cartItemEntity.getVariant();

        int updatedTotalQuantity = productEntity.getTotalQuantityReceived() + quantityToRestore;
        int updatedVariantQuantity = variant.getQuantity() + quantityToRestore;

        productEntity.setTotalQuantityReceived(updatedTotalQuantity);
        variant.setQuantity(updatedVariantQuantity);

        if (updatedTotalQuantity > 0) {
            productEntity.setAvailable(true);
        }

        if (updatedVariantQuantity > 0) {
            variant.setAvailable(true);
        }

            cartEntity.getCartItems().remove(cartItemEntity);
            cartItemRepository.delete(cartItemEntity);

            productRepository.save(productEntity);
            variantRepository.save(variant);

        int totalQuantity = cartEntity.getCartItems().stream().mapToInt(CartItemEntity::getQuantity).sum();

        CartResponse cartResponse = CartMapper.toCartDTOResponse(cartEntity, pageable);
        cartResponse.setTotalQuantity(totalQuantity);

        return cartResponse;
    }



}
