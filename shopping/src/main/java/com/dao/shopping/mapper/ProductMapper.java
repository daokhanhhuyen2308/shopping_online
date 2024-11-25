package com.dao.shopping.mapper;

import com.dao.shopping.dto.requests.ProductCreationRequest;
import com.dao.shopping.dto.responses.ProductResponse;
import com.dao.shopping.dto.responses.ProductVariantResponse;
import com.dao.shopping.entity.ProductEntity;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

public class ProductMapper {

    public static ProductResponse toProductDTOResponse(ProductEntity productEntity){

        Set<ProductVariantResponse> variantResponses = productEntity.getVariants().stream()
                .map(variant -> ProductVariantResponse.builder()
                        .variantId(variant.getId())
                        .color(ColorMapper.toColorResponse(variant.getColor()))
                        .size(SizeMapper.toSizeResponse(variant.getSize()))
                        .quantity(variant.getQuantity())
                        .isSoldOutVariant(variant.isSoldOut()).build())
                .collect(Collectors.toSet());

        return ProductResponse.builder()
                .id(productEntity.getId())
                .name(productEntity.getName()).image(productEntity.getImage())
                .description(productEntity.getDescription())
                .price(productEntity.getPrice())
                .brands(productEntity.getBrands().stream().map(BrandMapper::toBrandDTOResponse).collect(Collectors.toSet()))
                .category(CategoryMapper.toCategoryResponse(productEntity.getCategory()))
                .variants(variantResponses)
                .totalQuantityReceived(productEntity.getTotalQuantityReceived())
                .totalSoldQuantity(productEntity.getTotalSoldQuantity())
                .createdDate(productEntity.getCreatedDate())
                .createdBy(productEntity.getCreatedBy())
                .lastModifiedDate(productEntity.getLastModifiedDate())
                .lastModifiedBy(productEntity.getLastModifiedBy())
                .deleted(productEntity.getDeleted())
                .isSoldOutProduct(productEntity.isSoldOut())
                .slug(productEntity.getSlug())
                .build();
    }

    public static ProductEntity toProduct(ProductCreationRequest add){

        return ProductEntity.builder()
                .name(add.getName())
                .price(BigDecimal.valueOf(add.getPrice())).description(add.getDescription())
                .image(add.getImage())
                .build();
    }



}
