package com.example.order.service;

import com.example.order.dto.MenuItemDto;
import com.example.order.dto.OrderDto;
import com.example.order.enums.OrderStatus;
import com.example.order.exceptions.*;
import com.example.order.model.Order;
import com.example.order.model.OrderItem;
import com.example.order.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CatalogClientService catalogClientService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateOrder() {
        OrderItem item1 = new OrderItem(1L, "Pizza", 199.0, 2);
        OrderItem item2 = new OrderItem(2L, "Burger", 99.0, 1);
        List<OrderItem> orderItems = Arrays.asList(item1, item2);
        OrderDto orderDto = new OrderDto(1L, 1L, "Nizampet, Hyderabad", orderItems);

        MenuItemDto menuItemDto1 = new MenuItemDto(1L, "Pizza", 199.0);
        MenuItemDto menuItemDto2 = new MenuItemDto(2L, "Burger", 99.0);

        when(catalogClientService.getMenuItemByIdAndRestaurantId(1L, 1L)).thenReturn(menuItemDto1);
        when(catalogClientService.getMenuItemByIdAndRestaurantId(1L, 2L)).thenReturn(menuItemDto2);

        Order expectedOrder = new Order(1L, 1L, "Nizampet, Hyderabad", orderItems);
        when(orderRepository.save(any(Order.class))).thenReturn(expectedOrder);

        Order createdOrder = orderService.createOrder(orderDto.getRestaurantId(), orderDto.getCustomerId(), orderDto.getDeliveryAddress(), orderDto.getOrderItems());

        verify(orderRepository, times(1)).save(any(Order.class));
        assertEquals(497.0, createdOrder.getTotalPrice());
    }

    @Test
    void testCreateOrderWithNullRestaurantId() {
        OrderItem item1 = new OrderItem(1L, "Pizza", 199.0, 2);
        OrderItem item2 = new OrderItem(2L, "Burger", 99.0, 1);
        List<OrderItem> orderItems = Arrays.asList(item1, item2);
        OrderDto orderDto = new OrderDto(null, 1L, "Nizampet, Hyderabad", orderItems);

        Exception exception = assertThrows(RestaurantIdCannotBeNullOrNegativeException.class, () -> {
            orderService.createOrder(orderDto.getRestaurantId(), orderDto.getCustomerId(), orderDto.getDeliveryAddress(), orderDto.getOrderItems());
        });
        assertEquals("Restaurant ID cannot be null and must be greater than zero", exception.getMessage());
    }

    @Test
    void testCreateOrderWithNegativeRestaurantId() {
        OrderItem item1 = new OrderItem(1L, "Pizza", 199.0, 2);
        OrderItem item2 = new OrderItem(2L, "Burger", 99.0, 1);
        List<OrderItem> orderItems = Arrays.asList(item1, item2);
        OrderDto orderDto = new OrderDto(-1L, 1L, "Nizampet, Hyderabad", orderItems);

        Exception exception = assertThrows(RestaurantIdCannotBeNullOrNegativeException.class, () -> {
            orderService.createOrder(orderDto.getRestaurantId(), orderDto.getCustomerId(), orderDto.getDeliveryAddress(), orderDto.getOrderItems());
        });
        assertEquals("Restaurant ID cannot be null and must be greater than zero", exception.getMessage());
    }

    @Test
    void testCreateOrderWithNullCustomerId() {
        OrderItem item1 = new OrderItem(1L, "Pizza", 199.0, 2);
        OrderItem item2 = new OrderItem(2L, "Burger", 99.0, 1);
        List<OrderItem> orderItems = Arrays.asList(item1, item2);
        OrderDto orderDto = new OrderDto(1L, null, "Nizampet, Hyderabad", orderItems);

        MenuItemDto menuItemDto1 = new MenuItemDto(1L, "Pizza", 199.0);
        MenuItemDto menuItemDto2 = new MenuItemDto(2L, "Burger", 99.0);

        when(catalogClientService.getMenuItemByIdAndRestaurantId(1L, 1L)).thenReturn(menuItemDto1);
        when(catalogClientService.getMenuItemByIdAndRestaurantId(1L, 2L)).thenReturn(menuItemDto2);

        Exception exception = assertThrows(CustomerIdCannotBeNullOrNegativeException.class, () -> {
            orderService.createOrder(orderDto.getRestaurantId(), orderDto.getCustomerId(), orderDto.getDeliveryAddress(), orderDto.getOrderItems());
        });
        assertEquals("Customer ID cannot be null and must be greater than zero", exception.getMessage());
    }

    @Test
    void testCreateOrderWithNegativeCustomerId() {
        OrderItem item1 = new OrderItem(1L, "Pizza", 199.0, 2);
        OrderItem item2 = new OrderItem(2L, "Burger", 99.0, 1);
        List<OrderItem> orderItems = Arrays.asList(item1, item2);
        OrderDto orderDto = new OrderDto(1L, -1L, "Nizampet, Hyderabad", orderItems);

        MenuItemDto menuItemDto1 = new MenuItemDto(1L, "Pizza", 199.0);
        MenuItemDto menuItemDto2 = new MenuItemDto(2L, "Burger", 99.0);

        when(catalogClientService.getMenuItemByIdAndRestaurantId(1L, 1L)).thenReturn(menuItemDto1);
        when(catalogClientService.getMenuItemByIdAndRestaurantId(1L, 2L)).thenReturn(menuItemDto2);

        Exception exception = assertThrows(CustomerIdCannotBeNullOrNegativeException.class, () -> {
            orderService.createOrder(orderDto.getRestaurantId(), orderDto.getCustomerId(), orderDto.getDeliveryAddress(), orderDto.getOrderItems());
        });
        assertEquals("Customer ID cannot be null and must be greater than zero", exception.getMessage());
    }

    @Test
    void testCreateOrderWithNullDeliveryAddress() {
        OrderItem item1 = new OrderItem(1L, "Pizza", 199.0, 2);
        OrderItem item2 = new OrderItem(2L, "Burger", 99.0, 1);
        List<OrderItem> orderItems = Arrays.asList(item1, item2);
        OrderDto orderDto = new OrderDto(1L, 1L, null, orderItems);

        MenuItemDto menuItemDto1 = new MenuItemDto(1L, "Pizza", 199.0);
        MenuItemDto menuItemDto2 = new MenuItemDto(2L, "Burger", 99.0);

        when(catalogClientService.getMenuItemByIdAndRestaurantId(1L, 1L)).thenReturn(menuItemDto1);
        when(catalogClientService.getMenuItemByIdAndRestaurantId(1L, 2L)).thenReturn(menuItemDto2);

        Exception exception = assertThrows(DeliveryAddressCannotBeNullOrEmpty.class, () -> {
            orderService.createOrder(orderDto.getRestaurantId(), orderDto.getCustomerId(), orderDto.getDeliveryAddress(), orderDto.getOrderItems());
        });
        assertEquals("Delivery address cannot be null or empty", exception.getMessage());
    }

    @Test
    void testCreateOrderWithEmptyDeliveryAddress() {
        OrderItem item1 = new OrderItem(1L, "Pizza", 199.0, 2);
        OrderItem item2 = new OrderItem(2L, "Burger", 99.0, 1);
        List<OrderItem> orderItems = Arrays.asList(item1, item2);
        OrderDto orderDto = new OrderDto(1L, 1L, "", orderItems);

        MenuItemDto menuItemDto1 = new MenuItemDto(1L, "Pizza", 199.0);
        MenuItemDto menuItemDto2 = new MenuItemDto(2L, "Burger", 99.0);

        when(catalogClientService.getMenuItemByIdAndRestaurantId(1L, 1L)).thenReturn(menuItemDto1);
        when(catalogClientService.getMenuItemByIdAndRestaurantId(1L, 2L)).thenReturn(menuItemDto2);

        Exception exception = assertThrows(DeliveryAddressCannotBeNullOrEmpty.class, () -> {
            orderService.createOrder(orderDto.getRestaurantId(), orderDto.getCustomerId(), orderDto.getDeliveryAddress(), orderDto.getOrderItems());
        });
        assertEquals("Delivery address cannot be null or empty", exception.getMessage());
    }

    @Test
    void testCreateOrderWithNullItems() {
        OrderDto orderDto = new OrderDto(1L, 1L, "Nizampet, Hyderabad", null);

        Exception exception = assertThrows(OrderItemsCannotBeNullOrEmptyException.class, () -> {
            orderService.createOrder(orderDto.getRestaurantId(), orderDto.getCustomerId(), orderDto.getDeliveryAddress(), orderDto.getOrderItems());
        });
        assertEquals("Order items cannot be null or empty", exception.getMessage());
    }

    @Test
    void testCreateOrderWithEmptyItems() {
        OrderDto orderDto = new OrderDto(1L, 1L, "Nizampet, Hyderabad", Collections.emptyList());

        Exception exception = assertThrows(OrderItemsCannotBeNullOrEmptyException.class, () -> {
            orderService.createOrder(orderDto.getRestaurantId(), orderDto.getCustomerId(), orderDto.getDeliveryAddress(), orderDto.getOrderItems());
        });
        assertEquals("Order items cannot be null or empty", exception.getMessage());
    }

    @Test
    void testCreateOrderWhenMenuItemDoesNotExist() {
        OrderItem item1 = new OrderItem(10L, "Pizza", 199.0, 2);
        List<OrderItem> orderItems = List.of(item1);
        OrderDto orderDto = new OrderDto(1L, 1L, "Nizampet, Hyderabad", orderItems);

        when(catalogClientService.getMenuItemByIdAndRestaurantId(1L, 10L)).thenThrow(new MenuItemNotFoundException("Failed to retrieve the menu item with restaurant id: 1 and menu item id: 10"));

        Exception exception = assertThrows(MenuItemNotFoundException.class, () -> {
            orderService.createOrder(orderDto.getRestaurantId(), orderDto.getCustomerId(), orderDto.getDeliveryAddress(), orderDto.getOrderItems());
        });
        assertEquals("Menu item with restaurant id: 1 and menu item id: 10 is not found", exception.getMessage());
        verify(orderRepository, times(0)).save(any(Order.class));
    }

    @Test
    void testGetAllOrders() {
        OrderItem item1 = new OrderItem(1L, "Pizza", 199.0, 2);
        OrderItem item2 = new OrderItem(2L, "Burger", 99.0, 1);
        List<OrderItem> orderItems = Arrays.asList(item1, item2);

        Order order1 = new Order(1L, 1L, "Nizampet, Hyderabad", orderItems);
        Order order2 = new Order(2L, 1L, "Nizampet, Hyderabad", orderItems);
        List<Order> expectedOrders = Arrays.asList(order1, order2);

        when(orderRepository.findAll()).thenReturn(expectedOrders);

        List<Order> orders = orderService.getAllOrders();

        assertEquals(2, orders.size());
        assertEquals(expectedOrders, orders);
    }

    @Test
    void testGetOrdersWhenNoAllOrders() {
        when(orderRepository.findAll()).thenReturn(Collections.emptyList());

        List<Order> orders = orderService.getAllOrders();

        assertEquals(0, orders.size());
    }


    @Test
    void testGetOrderByIdSuccessfully() {
        OrderItem item1 = new OrderItem(1L, "Pizza", 199.0, 2);
        List<OrderItem> orderItems = Collections.singletonList(item1);
        Order expectedOrder = new Order(1L, 1L, "Nizampet, Hyderabad", orderItems);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(expectedOrder));

        Order order = orderService.getOrderById(1L);

        assertEquals(expectedOrder, order);
    }

    @Test
    void testGetOrderByIdWhenOrderNotFound() {
        when(orderRepository.findById(99L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(OrderNotFoundException.class, () -> {
            orderService.getOrderById(99L);
        });
        assertEquals("Order not found with id: 99", exception.getMessage());
    }

    @Test
    void testUpdateOrderStatusSuccessfully() {
        OrderItem item1 = new OrderItem(1L, "Pizza", 199.0, 2);
        List<OrderItem> orderItems = Collections.singletonList(item1);
        Order order = new Order(1L, 1L, "Nizampet, Hyderabad", orderItems);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(order)).thenReturn(order);

        Order updatedOrder = orderService.updateOrderStatus(1L);

        assertEquals(OrderStatus.OUT_FOR_DELIVERY, updatedOrder.getStatus());
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void testUpdateOrderStatusWhenOrderNotFound() {
        when(orderRepository.findById(99L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(OrderNotFoundException.class, () -> {
            orderService.updateOrderStatus(99L);
        });

        assertEquals("Order not found with id: 99", exception.getMessage());
        verify(orderRepository, times(0)).save(any(Order.class));
    }
}
