package com.dao.shopping.service;


import com.dao.shopping.dto.requests.OrderItemRequest;
import com.dao.shopping.dto.requests.OrderStatusUpdate;
import com.dao.shopping.dto.responses.OrderResponse;
import com.dao.shopping.dto.responses.OrderSummaryResponse;

import java.util.List;

public interface IOrderService {

    OrderResponse updateOrderStatus(Integer orderId, OrderStatusUpdate newStatus);

    List<OrderResponse> getAllOrdersOfAllUsers();

    List<OrderResponse> getOrderProduct();

    OrderSummaryResponse orderItem(OrderItemRequest request);

    OrderResponse getOrderDetailById(Integer orderId);

    OrderSummaryResponse getOrderById(Integer orderId);
}
