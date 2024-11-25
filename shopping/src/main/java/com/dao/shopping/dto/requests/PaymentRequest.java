package com.dao.shopping.dto.requests;

import com.dao.shopping.enums.PaymentType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentRequest {
    PaymentType paymentMethod;
    BigDecimal amount;
}
