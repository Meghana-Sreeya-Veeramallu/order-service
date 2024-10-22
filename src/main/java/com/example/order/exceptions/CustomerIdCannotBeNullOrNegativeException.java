package com.example.order.exceptions;

public class CustomerIdCannotBeNullOrNegativeException extends RuntimeException {
    public CustomerIdCannotBeNullOrNegativeException(String message) {
        super(message);
    }
}
