package com.example.order.exceptions;

public class MenuItemIdCannotBeNullOrNegativeException extends RuntimeException {
    public MenuItemIdCannotBeNullOrNegativeException(String message) {
        super(message);
    }
}
