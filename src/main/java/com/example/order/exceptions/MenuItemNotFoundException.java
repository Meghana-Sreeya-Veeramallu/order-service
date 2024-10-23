package com.example.order.exceptions;

public class MenuItemNotFoundException extends RuntimeException {
    public MenuItemNotFoundException(String message) {
        super(message);
    }
}
