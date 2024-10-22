package com.example.order.model;

import com.example.order.enums.OrderStatus;
import com.example.order.exceptions.CustomerIdCannotBeNullOrNegativeException;
import com.example.order.exceptions.OrderItemsCannotBeNullOrEmptyException;
import com.example.order.exceptions.RestaurantIdCannotBeNullOrNegativeException;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long restaurantId;
    private Long customerId;
    @Getter
    private double totalPrice;

    @Getter
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    private List<OrderItem> orderItems;

    public Order(Long restaurantId, Long customerId, List<OrderItem> orderItems) {
        if (restaurantId == null || restaurantId <= 0) {
            throw new RestaurantIdCannotBeNullOrNegativeException("Restaurant ID cannot be null and must be greater than zero");
        }
        if (customerId == null || customerId <= 0) {
            throw new CustomerIdCannotBeNullOrNegativeException("Customer ID cannot be null and must be greater than zero");
        }
        if (orderItems == null || orderItems.isEmpty()) {
            throw new OrderItemsCannotBeNullOrEmptyException("Order items cannot be null or empty");
        }
        this.restaurantId = restaurantId;
        this.customerId = customerId;
        this.totalPrice = calculateTotalPrice(orderItems);
        this.status = OrderStatus.CREATED;
        this.orderItems = orderItems;
    }

    public Order() {}

    private double calculateTotalPrice(List<OrderItem> orderItems) {
        double total = 0;
        for (OrderItem item : orderItems) {
            total += item.getPrice() * item.getQuantity();
        }
        return total;
    }
}
