package com.example.order.exceptions;

public class CannotUpdateOrderStatusException extends RuntimeException {
    public CannotUpdateOrderStatusException(String message) {
        super(message);
    }
}
