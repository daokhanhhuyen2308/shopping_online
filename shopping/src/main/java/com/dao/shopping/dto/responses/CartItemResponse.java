package com.dao.shopping.dto.responses;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartItemResponse {
     Integer cartItemId;
     Integer productId;
     String productImage;
     String productName;
     ProductVariantResponse variant;
     Integer productQuantity;
     BigDecimal productPrice;
     Float salePercentage;
     BigDecimal discountedPrice;
     boolean isAvailable;

}
