package com.dao.shopping.dto.requests;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Set;


@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductUpdateRequest {
    String name;
    String image;
    String description;
    Double price;
    Set<Integer> brandIds;
    Integer categoryId;
    ProductVariantUpdate variant;
    Integer saleId;
}
