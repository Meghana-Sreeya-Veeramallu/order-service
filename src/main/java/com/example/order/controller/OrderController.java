package com.example.order.controller;

import com.example.order.dto.OrderDto;
import com.example.order.model.Order;
import com.example.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<String> createOrder(@RequestBody OrderDto orderDto) {
        Order createdOrder = orderService.createOrder(orderDto);
        String successMessage = "Order created successfully with total price " + createdOrder.getTotalPrice();
        return ResponseEntity.ok(successMessage);
    }
}
