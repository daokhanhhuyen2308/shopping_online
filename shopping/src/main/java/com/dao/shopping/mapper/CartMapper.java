package com.dao.shopping.mapper;


import com.dao.shopping.dto.responses.CartItemResponse;
import com.dao.shopping.dto.responses.CartResponse;
import com.dao.shopping.dto.responses.ResponsePage;
import com.dao.shopping.entity.CartEntity;
import com.dao.shopping.entity.CartItemEntity;
import org.springframework.data.domain.Pageable;

import java.util.stream.Collectors;


public class CartMapper {

    public static CartResponse toCartDTOResponse(CartEntity cartEntity, Pageable pageable){

        ResponsePage<CartItemResponse> cartItemResponseResponsePage = new ResponsePage<>();
        cartItemResponseResponsePage.setContent(cartEntity.getCartItems()
                .stream().map(CartMapper::toCartItemDTOResponse).collect(Collectors.toList()));
        cartItemResponseResponsePage.setPageNumber(pageable.getPageNumber());
        cartItemResponseResponsePage.setPageSize(pageable.getPageSize());
        cartItemResponseResponsePage.setTotalElements(cartEntity.getCartItems().size());
        cartItemResponseResponsePage.setTotalPages(cartEntity.getCartItems().size() / pageable.getPageSize());

        if (cartEntity.getCartItems().isEmpty()){
            cartItemResponseResponsePage.setContent(null);
        }

        return CartResponse.builder()
                .cartId(cartEntity.getId())
                .cartItem(cartItemResponseResponsePage)
                .totalQuantity(cartEntity.getTotalQuantity())
                .createdDate(cartEntity.getCreatedDate())
                .createdBy(cartEntity.getCreatedBy())
                .lastModifiedBy(cartEntity.getLastModifiedBy())
                .lastModifiedDate(cartEntity.getLastModifiedDate())
                .deleted(cartEntity.getDeleted())
                .build();
    }

    public static CartItemResponse toCartItemDTOResponse(CartItemEntity cartItemEntity){

        int productQuantity = cartItemEntity.getVariant().isAvailable() ? cartItemEntity.getQuantity() : 0;

        return CartItemResponse.builder()
                .cartItemId(cartItemEntity.getId())
                .productId(cartItemEntity.getVariant().getProduct().getId())
                .productImage(cartItemEntity.getVariant().getProduct().getImage())
                .productName(cartItemEntity.getVariant().getProduct().getName())
                .variant(ProductVariantMapper.toProductVariantResponse(cartItemEntity.getVariant()))
                .productQuantity(productQuantity)
                .productPrice(cartItemEntity.getPrice())
                .discountedPrice(cartItemEntity.getDiscountedPrice())
                .isAvailable(cartItemEntity.getVariant().isAvailable())
                .build();
    }


}
