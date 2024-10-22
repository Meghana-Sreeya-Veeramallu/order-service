package com.example.order.exceptions;

public class MenuItemNameCannotBeNullOrEmptyException extends RuntimeException {
    public MenuItemNameCannotBeNullOrEmptyException(String message) {
        super(message);
    }
}
