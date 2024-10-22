package com.example.order.service;

import com.example.order.dto.OrderDto;
import com.example.order.exceptions.CustomerIdCannotBeNullOrNegativeException;
import com.example.order.exceptions.OrderItemsCannotBeNullOrEmptyException;
import com.example.order.exceptions.RestaurantIdCannotBeNullOrNegativeException;
import com.example.order.model.Order;
import com.example.order.model.OrderItem;
import com.example.order.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateOrder() {
        OrderItem item1 = new OrderItem(1L, "Pizza", 199.0, 2);
        OrderItem item2 = new OrderItem(2L, "Burger", 99.0, 1);
        List<OrderItem> orderItems = Arrays.asList(item1, item2);
        OrderDto order = new OrderDto(1L, 1L, orderItems);

        Order expectedOrder = new Order(1L, 1L, orderItems);
        when(orderRepository.save(any(Order.class))).thenReturn(expectedOrder);

        Order createdOrder = orderService.createOrder(order);

        verify(orderRepository, times(1)).save(any(Order.class));
        assertEquals(497.0, createdOrder.getTotalPrice());
    }

    @Test
    void testCreateOrderWithNullRestaurantId() {
        OrderItem item1 = new OrderItem(1L, "Pizza", 199.0, 2);
        OrderItem item2 = new OrderItem(2L, "Burger", 99.0, 1);
        List<OrderItem> orderItems = Arrays.asList(item1, item2);
        OrderDto orderDto = new OrderDto(null, 1L, orderItems);

        Exception exception = assertThrows(RestaurantIdCannotBeNullOrNegativeException.class, () -> {
            orderService.createOrder(orderDto);
        });
        assertEquals("Restaurant ID cannot be null and must be greater than zero", exception.getMessage());
    }

    @Test
    void testCreateOrderWithNegativeRestaurantId() {
        OrderItem item1 = new OrderItem(1L, "Pizza", 199.0, 2);
        OrderItem item2 = new OrderItem(2L, "Burger", 99.0, 1);
        List<OrderItem> orderItems = Arrays.asList(item1, item2);
        OrderDto orderDto = new OrderDto(-1L, 1L, orderItems);

        Exception exception = assertThrows(RestaurantIdCannotBeNullOrNegativeException.class, () -> {
            orderService.createOrder(orderDto);
        });
        assertEquals("Restaurant ID cannot be null and must be greater than zero", exception.getMessage());
    }

    @Test
    void testCreateOrderWithNullCustomerId() {
        OrderItem item1 = new OrderItem(1L, "Pizza", 199.0, 2);
        OrderItem item2 = new OrderItem(2L, "Burger", 99.0, 1);
        List<OrderItem> orderItems = Arrays.asList(item1, item2);
        OrderDto orderDto = new OrderDto(1L, null, orderItems);

        Exception exception = assertThrows(CustomerIdCannotBeNullOrNegativeException.class, () -> {
            orderService.createOrder(orderDto);
        });
        assertEquals("Customer ID cannot be null and must be greater than zero", exception.getMessage());
    }

    @Test
    void testCreateOrderWithNegativeCustomerId() {
        OrderItem item1 = new OrderItem(1L, "Pizza", 199.0, 2);
        OrderItem item2 = new OrderItem(2L, "Burger", 99.0, 1);
        List<OrderItem> orderItems = Arrays.asList(item1, item2);
        OrderDto orderDto = new OrderDto(1L, -1L, orderItems);

        Exception exception = assertThrows(CustomerIdCannotBeNullOrNegativeException.class, () -> {
            orderService.createOrder(orderDto);
        });
        assertEquals("Customer ID cannot be null and must be greater than zero", exception.getMessage());
    }

    @Test
    void testCreateOrderWithNullItems() {
        OrderDto orderDto = new OrderDto(1L, 1L, null);

        Exception exception = assertThrows(OrderItemsCannotBeNullOrEmptyException.class, () -> {
            orderService.createOrder(orderDto);
        });
        assertEquals("Order items cannot be null or empty", exception.getMessage());
    }

    @Test
    void testCreateOrderWithEmptyItems() {
        OrderDto orderDto = new OrderDto(1L, 1L, Collections.emptyList());

        Exception exception = assertThrows(OrderItemsCannotBeNullOrEmptyException.class, () -> {
            orderService.createOrder(orderDto);
        });
        assertEquals("Order items cannot be null or empty", exception.getMessage());
    }
}
