package com.example.order.model;

import com.example.order.enums.OrderStatus;
import com.example.order.exceptions.CustomerIdCannotBeNullOrNegativeException;
import com.example.order.exceptions.DeliveryAddressCannotBeNullOrEmpty;
import com.example.order.exceptions.OrderItemsCannotBeNullOrEmptyException;
import com.example.order.exceptions.RestaurantIdCannotBeNullOrNegativeException;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    @Test
    void testValidOrder() {
        OrderItem item1 = new OrderItem(1L, "Pizza", 199, 2);
        OrderItem item2 = new OrderItem(2L, "Burger", 99, 3);
        Order order = new Order(1L, 1L, "Nizampet, Hyderabad", Arrays.asList(item1, item2));

        assertNotNull(order);
        assertEquals(695, order.getTotalPrice());
        assertEquals(OrderStatus.CREATED, order.getStatus());
    }

    @Test
    void testRestaurantIdCannotBeNull() {
        RestaurantIdCannotBeNullOrNegativeException exception = assertThrows(RestaurantIdCannotBeNullOrNegativeException.class, () -> {
            new Order(null, 1L, "Nizampet, Hyderabad",Collections.singletonList(new OrderItem(1L, "Pizza", 10.99, 2)));
        });
        assertEquals("Restaurant ID cannot be null and must be greater than zero", exception.getMessage());
    }

    @Test
    void testRestaurantIdCannotBeNegative() {
        RestaurantIdCannotBeNullOrNegativeException exception = assertThrows(RestaurantIdCannotBeNullOrNegativeException.class, () -> {
            new Order(-1L, 1L, "Nizampet, Hyderabad",Collections.singletonList(new OrderItem(1L, "Pizza", 10.99, 2)));
        });
        assertEquals("Restaurant ID cannot be null and must be greater than zero", exception.getMessage());
    }

    @Test
    void testCustomerIdCannotBeNull() {
        CustomerIdCannotBeNullOrNegativeException exception = assertThrows(CustomerIdCannotBeNullOrNegativeException.class, () -> {
            new Order(1L, null, "Nizampet, Hyderabad",Collections.singletonList(new OrderItem(1L, "Pizza", 10.99, 2)));
        });
        assertEquals("Customer ID cannot be null and must be greater than zero", exception.getMessage());
    }

    @Test
    void testCustomerIdCannotBeNegative() {
        CustomerIdCannotBeNullOrNegativeException exception = assertThrows(CustomerIdCannotBeNullOrNegativeException.class, () -> {
            new Order(1L, -1L, "Nizampet, Hyderabad",Collections.singletonList(new OrderItem(1L, "Pizza", 10.99, 2)));
        });
        assertEquals("Customer ID cannot be null and must be greater than zero", exception.getMessage());
    }

    @Test
    void testDeliveryAddressCannotBeNull() {
        DeliveryAddressCannotBeNullOrEmpty exception = assertThrows(DeliveryAddressCannotBeNullOrEmpty.class, () -> {
            new Order(1L, 1L, null,Collections.singletonList(new OrderItem(1L, "Pizza", 10.99, 2)));
        });
        assertEquals("Delivery address cannot be null or empty", exception.getMessage());
    }

    @Test
    void testDeliveryAddressCannotBeEmpty() {
        DeliveryAddressCannotBeNullOrEmpty exception = assertThrows(DeliveryAddressCannotBeNullOrEmpty.class, () -> {
            new Order(1L, 1L, " ",Collections.singletonList(new OrderItem(1L, "Pizza", 10.99, 2)));
        });
        assertEquals("Delivery address cannot be null or empty", exception.getMessage());
    }

    @Test
    void testOrderItemsCannotBeNull() {
        OrderItemsCannotBeNullOrEmptyException exception = assertThrows(OrderItemsCannotBeNullOrEmptyException.class, () -> {
            new Order(1L, 1L, "Nizampet, Hyderabad",null);
        });
        assertEquals("Order items cannot be null or empty", exception.getMessage());
    }

    @Test
    void testOrderItemsCannotBeEmpty() {
        OrderItemsCannotBeNullOrEmptyException exception = assertThrows(OrderItemsCannotBeNullOrEmptyException.class, () -> {
            new Order(1L, 1L, "Nizampet, Hyderabad",Collections.emptyList());
        });
        assertEquals("Order items cannot be null or empty", exception.getMessage());
    }
}
