package com.dao.shopping.dto.requests;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductVariantUpdate {
    Integer variantId;
    Integer colorId;
    Integer sizeId;
    Integer quantity;
}
