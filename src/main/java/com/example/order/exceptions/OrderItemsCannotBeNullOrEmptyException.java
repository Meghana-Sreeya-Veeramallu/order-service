package com.example.order.exceptions;

public class OrderItemsCannotBeNullOrEmptyException extends RuntimeException {
    public OrderItemsCannotBeNullOrEmptyException(String message) {
        super(message);
    }
}
