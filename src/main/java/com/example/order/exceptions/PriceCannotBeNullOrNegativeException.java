package com.example.order.exceptions;

public class PriceCannotBeNullOrNegativeException extends RuntimeException {
    public PriceCannotBeNullOrNegativeException(String message) {
        super(message);
    }
}
