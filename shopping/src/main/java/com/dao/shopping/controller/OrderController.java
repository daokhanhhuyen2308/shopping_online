package com.dao.shopping.controller;

import com.dao.shopping.dto.requests.OrderItemRequest;
import com.dao.shopping.dto.requests.OrderStatusUpdate;
import com.dao.shopping.dto.responses.OrderResponse;
import com.dao.shopping.dto.responses.OrderSummaryResponse;
import com.dao.shopping.service.IOrderService;
import com.dao.shopping.validator.Admin;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {

    private final IOrderService iOrderService;

    @GetMapping("/detail/{orderId}")
    @Admin
    public ResponseEntity<OrderResponse> getOrderDetailById(@PathVariable Integer orderId) {
        return ResponseEntity.ok(iOrderService.getOrderDetailById(orderId));
    }

    @GetMapping("/{orderId}/")
    public ResponseEntity<OrderSummaryResponse> getOrderById(@PathVariable Integer orderId) {
        return ResponseEntity.ok(iOrderService.getOrderById(orderId));
    }

    @PostMapping("/add")
    public ResponseEntity<OrderSummaryResponse> orderItem(@RequestBody OrderItemRequest request){
        return ResponseEntity.ok(iOrderService.orderItem(request));
    }

    @PutMapping("/status/{orderId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANGER')")
    public ResponseEntity<OrderResponse> updateOrderStatus(@PathVariable Integer orderId ,@RequestBody OrderStatusUpdate newStatus){
        return ResponseEntity.ok(iOrderService.updateOrderStatus(orderId, newStatus));
    }

    @GetMapping("/all-user-orders")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    public List<OrderResponse> getAllOrdersOfAllUsers(){
        return iOrderService.getAllOrdersOfAllUsers();
    }

    @GetMapping("/all-orders")
    public List<OrderResponse> getOrderProduct(){
        return iOrderService.getOrderProduct();
    }


}
