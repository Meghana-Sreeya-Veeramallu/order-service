package com.example.order.exceptions;

public class DeliveryAddressCannotBeNullOrEmpty extends RuntimeException {
    public DeliveryAddressCannotBeNullOrEmpty(String message) {
        super(message);
    }
}
