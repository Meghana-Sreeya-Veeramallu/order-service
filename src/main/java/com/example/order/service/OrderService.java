package com.example.order.service;

import com.example.order.dto.MenuItemDto;
import com.example.order.enums.OrderStatus;
import com.example.order.exceptions.*;
import com.example.order.model.Order;
import com.example.order.model.OrderItem;
import com.example.order.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final CatalogClientService catalogClientService;

    @Autowired
    public OrderService(OrderRepository orderRepository, CatalogClientService catalogClientService) {
        this.orderRepository = orderRepository;
        this.catalogClientService = catalogClientService;
    }

    public Order createOrder(Long restaurantId, Long customerId, String deliveryAddress, List<OrderItem> orderItems) {
        if (restaurantId == null || restaurantId <= 0) {
            throw new RestaurantIdCannotBeNullOrNegativeException("Restaurant ID cannot be null and must be greater than zero");
        }
        if (orderItems == null || orderItems.isEmpty()) {
            throw new OrderItemsCannotBeNullOrEmptyException("Order items cannot be null or empty");
        }

        List<OrderItem> mappedOrderItems = orderItems.stream()
                .map(item -> {
                    MenuItemDto menuItemDto = getMenuItemByIdAndRestaurantId(restaurantId, item.getMenuItemId(), catalogClientService);
                    return new OrderItem(item.getMenuItemId(), menuItemDto.getName(), menuItemDto.getPrice(), item.getQuantity());
                })
                .collect(Collectors.toList());

        Order order = new Order(restaurantId, customerId, deliveryAddress, mappedOrderItems);
        return orderRepository.save(order);
    }

    private MenuItemDto getMenuItemByIdAndRestaurantId(Long restaurantId, Long menuItemId, CatalogClientService catalogClientService) {
        if (menuItemId == null || menuItemId <= 0) {
            throw new MenuItemIdCannotBeNullOrNegativeException("Menu item ID cannot be null and must be greater than zero");
        }
        MenuItemDto menuItem;
        try {
            menuItem = catalogClientService.getMenuItemByIdAndRestaurantId(restaurantId, menuItemId);
        } catch (Exception e) {
            throw new MenuItemNotFoundException("Menu item with restaurant id: "+ restaurantId + " and menu item id: " + menuItemId + " is not found");
        }
        return menuItem;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + orderId));
    }

    public Order updateOrderStatus(Long orderId) {
        Order order = getOrderById(orderId);

        if (order.getStatus() != OrderStatus.CREATED) {
            throw new CannotUpdateOrderStatusException("Order status can only be updated from CREATED to OUT FOR DELIVERY");
        }

        order.setStatus(OrderStatus.OUT_FOR_DELIVERY);
        return orderRepository.save(order);
    }
}
