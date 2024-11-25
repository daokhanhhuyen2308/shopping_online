package com.dao.shopping.dto.requests;

import com.dao.shopping.enums.OrderStatusType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderStatusUpdate {
    OrderStatusType status;
}
