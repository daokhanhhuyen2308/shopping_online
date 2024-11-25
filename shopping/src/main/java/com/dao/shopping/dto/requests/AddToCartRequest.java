package com.dao.shopping.dto.requests;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;


@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddToCartRequest {
    Integer productId;
    CartVariantRequest variant;
}
