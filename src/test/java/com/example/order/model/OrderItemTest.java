package com.example.order.model;

import com.example.order.exceptions.MenuItemIdCannotBeNullOrNegativeException;
import com.example.order.exceptions.MenuItemNameCannotBeNullOrEmptyException;
import com.example.order.exceptions.PriceCannotBeNullOrNegativeException;
import com.example.order.exceptions.QuantityCannotBeNullOrNegativeException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderItemTest {

    @Test
    void testValidOrderItem() {
        OrderItem orderItem = new OrderItem(1L, "Pizza", 199, 2);
        assertNotNull(orderItem);
    }

    @Test
    void testMenuItemIdCannotBeNull() {
        MenuItemIdCannotBeNullOrNegativeException exception = assertThrows(MenuItemIdCannotBeNullOrNegativeException.class, () -> {
            new OrderItem(null, "Pizza", 199, 2);
        });
        assertEquals("Menu item ID cannot be null and must be greater than zero", exception.getMessage());
    }

    @Test
    void testMenuItemIdCannotBeNegative() {
        MenuItemIdCannotBeNullOrNegativeException exception = assertThrows(MenuItemIdCannotBeNullOrNegativeException.class, () -> {
            new OrderItem(-1L, "Pizza", 199, 2);
        });
        assertEquals("Menu item ID cannot be null and must be greater than zero", exception.getMessage());
    }

    @Test
    void testMenuItemNameCannotBeNull() {
        MenuItemNameCannotBeNullOrEmptyException exception = assertThrows(MenuItemNameCannotBeNullOrEmptyException.class, () -> {
            new OrderItem(1L, null, 199, 2);
        });
        assertEquals("Menu item name cannot be null or empty", exception.getMessage());
    }

    @Test
    void testMenuItemNameCannotBeEmpty() {
        MenuItemNameCannotBeNullOrEmptyException exception = assertThrows(MenuItemNameCannotBeNullOrEmptyException.class, () -> {
            new OrderItem(1L, "", 199, 2);
        });
        assertEquals("Menu item name cannot be null or empty", exception.getMessage());
    }

    @Test
    void testPriceCannotBeZero() {
        PriceCannotBeNullOrNegativeException exception = assertThrows(PriceCannotBeNullOrNegativeException.class, () -> {
            new OrderItem(1L, "Pizza", 0, 2);
        });
        assertEquals("Price cannot be null and must be greater than zero", exception.getMessage());
    }

    @Test
    void testPriceCannotBeNegative() {
        PriceCannotBeNullOrNegativeException exception = assertThrows(PriceCannotBeNullOrNegativeException.class, () -> {
            new OrderItem(1L, "Pizza", -199, 2);
        });
        assertEquals("Price cannot be null and must be greater than zero", exception.getMessage());
    }

    @Test
    void testQuantityCannotBeZero() {
        QuantityCannotBeNullOrNegativeException exception = assertThrows(QuantityCannotBeNullOrNegativeException.class, () -> {
            new OrderItem(1L, "Pizza", 199, 0);
        });
        assertEquals("Quantity cannot be null and must be greater than zero", exception.getMessage());
    }

    @Test
    void testQuantityCannotBeNegative() {
        QuantityCannotBeNullOrNegativeException exception = assertThrows(QuantityCannotBeNullOrNegativeException.class, () -> {
            new OrderItem(1L, "Pizza", 199, -1);
        });
        assertEquals("Quantity cannot be null and must be greater than zero", exception.getMessage());
    }
}
