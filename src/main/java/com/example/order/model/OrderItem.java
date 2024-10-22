package com.example.order.model;

import com.example.order.exceptions.MenuItemIdCannotBeNullOrNegativeException;
import com.example.order.exceptions.MenuItemNameCannotBeNullOrEmptyException;
import com.example.order.exceptions.PriceCannotBeNullOrNegativeException;
import com.example.order.exceptions.QuantityCannotBeNullOrNegativeException;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "orderItems")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long menuItemId;
    private String menuItemName;
    @Getter
    private double price;
    @Getter
    private int quantity;

    public OrderItem(Long menuItemId, String menuItemName, double price, int quantity) {
        if (menuItemId == null || menuItemId <= 0) {
            throw new MenuItemIdCannotBeNullOrNegativeException("Menu item ID cannot be null and must be greater than zero");
        }
        if (menuItemName == null || menuItemName.isEmpty()) {
            throw new MenuItemNameCannotBeNullOrEmptyException("Menu item name cannot be null or empty");
        }
        if (price <= 0) {
            throw new PriceCannotBeNullOrNegativeException("Price cannot be null and must be greater than zero");
        }
        if (quantity <= 0) {
            throw new QuantityCannotBeNullOrNegativeException("Quantity cannot be null and must be greater than zero");
        }
        this.menuItemId = menuItemId;
        this.menuItemName = menuItemName;
        this.price = price;
        this.quantity = quantity;
    }

    public OrderItem() {}
}
