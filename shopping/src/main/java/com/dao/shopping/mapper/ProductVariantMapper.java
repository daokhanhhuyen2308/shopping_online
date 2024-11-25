package com.dao.shopping.mapper;


import com.dao.shopping.dto.responses.ProductVariantResponse;
import com.dao.shopping.entity.ProductVariantEntity;

public class ProductVariantMapper {

    public static ProductVariantResponse toProductVariantResponse(ProductVariantEntity variant) {
        return ProductVariantResponse.builder().variantId(variant.getId())
                .color(ColorMapper.toColorResponse(variant.getColor()))
                .size(SizeMapper.toSizeResponse(variant.getSize()))
                .isSoldOutVariant(variant.isSoldOut())
                .quantity(variant.getQuantity())
                .build();
    }
}
