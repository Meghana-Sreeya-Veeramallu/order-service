package com.example.order.model;

import com.example.order.enums.OrderStatus;
import com.example.order.exceptions.CustomerIdCannotBeNullOrNegativeException;
import com.example.order.exceptions.DeliveryAddressCannotBeNullOrEmpty;
import com.example.order.exceptions.OrderItemsCannotBeNullOrEmptyException;
import com.example.order.exceptions.RestaurantIdCannotBeNullOrNegativeException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long restaurantId;
    private Long customerId;
    private double totalPrice;

    private String deliveryAddress;

    @Setter
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    private List<OrderItem> orderItems;

    public Order(Long restaurantId, Long customerId, String deliveryAddress, List<OrderItem> orderItems) {
        if (restaurantId == null || restaurantId <= 0) {
            throw new RestaurantIdCannotBeNullOrNegativeException("Restaurant ID cannot be null and must be greater than zero");
        }
        if (customerId == null || customerId <= 0) {
            throw new CustomerIdCannotBeNullOrNegativeException("Customer ID cannot be null and must be greater than zero");
        }
        if (deliveryAddress == null || deliveryAddress.isBlank()) {
            throw new DeliveryAddressCannotBeNullOrEmpty("Delivery address cannot be null or empty");
        }
        if (orderItems == null || orderItems.isEmpty()) {
            throw new OrderItemsCannotBeNullOrEmptyException("Order items cannot be null or empty");
        }
        this.restaurantId = restaurantId;
        this.customerId = customerId;
        this.deliveryAddress = deliveryAddress;
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
