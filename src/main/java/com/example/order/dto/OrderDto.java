package com.example.order.dto;

import com.example.order.model.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class OrderDto {
    private Long restaurantId;
    private Long customerId;
    private String deliveryAddress;
    private List<OrderItem> orderItems;
}
