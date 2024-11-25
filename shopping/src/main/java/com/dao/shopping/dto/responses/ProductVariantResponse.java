package com.dao.shopping.dto.responses;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ProductVariantResponse {
    private Integer variantId;
    private ColorResponse color;
    private SizeResponse size;
    private Integer quantity;
    private boolean isSoldOutVariant;
}
