package com.example.order.exceptions;

public class RestaurantIdCannotBeNullOrNegativeException extends RuntimeException {
    public RestaurantIdCannotBeNullOrNegativeException(String message) {
        super(message);
    }
}
