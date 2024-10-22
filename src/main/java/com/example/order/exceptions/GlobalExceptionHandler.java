package com.example.order.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomerIdCannotBeNullOrNegativeException.class)
    public ResponseEntity<String> handleCustomerIdCannotBeNullOrNegative(CustomerIdCannotBeNullOrNegativeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad Request: " + ex.getMessage());
    }

    @ExceptionHandler(MenuItemIdCannotBeNullOrNegativeException.class)
    public ResponseEntity<String> handleMenuItemIdCannotBeNullOrNegative(MenuItemIdCannotBeNullOrNegativeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad Request: " + ex.getMessage());
    }

    @ExceptionHandler(MenuItemNameCannotBeNullOrEmptyException.class)
    public ResponseEntity<String> handleMenuItemNameCannotBeNullOrEmpty(MenuItemNameCannotBeNullOrEmptyException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad Request: " + ex.getMessage());
    }

    @ExceptionHandler(OrderItemsCannotBeNullOrEmptyException.class)
    public ResponseEntity<String> handleOrderItemsCannotBeNullOrEmpty(OrderItemsCannotBeNullOrEmptyException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad Request: " + ex.getMessage());
    }

    @ExceptionHandler(PriceCannotBeNullOrNegativeException.class)
    public ResponseEntity<String> handlePriceCannotBeNullOrNegative(PriceCannotBeNullOrNegativeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad Request: " + ex.getMessage());
    }

    @ExceptionHandler(QuantityCannotBeNullOrNegativeException.class)
    public ResponseEntity<String> handleQuantityCannotBeNullOrNegative(QuantityCannotBeNullOrNegativeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad Request: " + ex.getMessage());
    }

    @ExceptionHandler(RestaurantIdCannotBeNullOrNegativeException.class)
    public ResponseEntity<String> handleRestaurantIdCannotBeNullOrNegative(RestaurantIdCannotBeNullOrNegativeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad Request: " + ex.getMessage());
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<String> handleOrderNotFound(OrderNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not Found: " + ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception e) {
        return ResponseEntity.internalServerError().body("An error occurred: " + e.getMessage());
    }
}
