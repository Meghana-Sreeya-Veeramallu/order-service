package com.example.order.exceptions;

public class QuantityCannotBeNullOrNegativeException extends RuntimeException {
    public QuantityCannotBeNullOrNegativeException(String message) {
        super(message);
    }
}
