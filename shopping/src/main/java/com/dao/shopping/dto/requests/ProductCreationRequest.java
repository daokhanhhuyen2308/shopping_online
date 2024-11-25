package com.dao.shopping.dto.requests;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductCreationRequest {
    String name;
    String image;
    String description;
    Double price;
    Set<Integer> brandIds;
    Integer categoryId;
    Set<ProductVariantRequest> variantDTOSet;
    Integer saleId;
}
