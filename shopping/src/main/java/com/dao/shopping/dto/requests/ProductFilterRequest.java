package com.dao.shopping.dto.requests;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;


@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductFilterRequest {
    String name;
    String brandName;
    String category;
    String color;
    Double minPrice;
    Double maxPrice;
    Integer page;
    Integer size;

}
