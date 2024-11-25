package com.dao.shopping.dto.requests;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CartItemUpdateRequest {
    private Integer newQuantity;
}
