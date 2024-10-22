package com.example.order.service;

import com.example.order.dto.OrderDto;
import com.example.order.exceptions.OrderItemsCannotBeNullOrEmptyException;
import com.example.order.model.Order;
import com.example.order.model.OrderItem;
import com.example.order.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order createOrder(OrderDto orderDto) {
        List<OrderItem> orderItems = orderDto.getOrderItems();

        if (orderItems == null || orderItems.isEmpty()) {
            throw new OrderItemsCannotBeNullOrEmptyException("Order items cannot be null or empty");
        }

        List<OrderItem> mappedOrderItems = orderItems.stream()
                .map(item -> new OrderItem(item.getMenuItemId(), item.getMenuItemName(), item.getPrice(), item.getQuantity()))
                .collect(Collectors.toList());

        Order order = new Order(orderDto.getRestaurantId(), orderDto.getCustomerId(), mappedOrderItems);
        return orderRepository.save(order);
    }
}
