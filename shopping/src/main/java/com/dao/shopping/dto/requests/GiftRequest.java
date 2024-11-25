package com.dao.shopping.dto.requests;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class GiftRequest {
    String giftName;
    String description;
    BigDecimal price;
    Integer voucherId;
}
