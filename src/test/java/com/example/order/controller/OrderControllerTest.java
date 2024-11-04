package com.example.order.controller;

import com.example.order.dto.OrderDto;
import com.example.order.exceptions.*;
import com.example.order.model.Order;
import com.example.order.model.OrderItem;
import com.example.order.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class OrderControllerTest {

    @InjectMocks
    private OrderController orderController;

    @Mock
    private OrderService orderService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).setControllerAdvice(new GlobalExceptionHandler()).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testCreateOrderSuccessfully() throws Exception {
        OrderItem item1 = new OrderItem(1L, "Pizza", 199.0, 2);
        OrderItem item2 = new OrderItem(2L, "Burger", 99.0, 1);
        List<OrderItem> orderItems = Arrays.asList(item1, item2);
        OrderDto orderDto = new OrderDto(1L, 1L, "Nizampet, Hyderabad", orderItems);
        String jsonRequestBody = objectMapper.writeValueAsString(orderDto);

        Order createdOrder = new Order(1L, 1L, "Nizampet, Hyderabad", orderItems);
        when(orderService.createOrder(any(), any(), any(), any())).thenReturn(createdOrder);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestBody))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString();
        assertEquals("Order created successfully with total price 497.0", responseBody);
        verify(orderService, times(1)).createOrder(any(), any(), any(), any());
    }

    @Test
    void testCreateOrderWithNullRestaurantId() throws Exception {
        OrderItem item1 = new OrderItem(1L, "Pizza", 199.0, 2);
        OrderItem item2 = new OrderItem(2L, "Burger", 99.0, 1);
        List<OrderItem> orderItems = Arrays.asList(item1, item2);
        OrderDto orderDto = new OrderDto(null, 1L, "Nizampet, Hyderabad", orderItems);
        String jsonRequestBody = objectMapper.writeValueAsString(orderDto);

        doThrow(new RestaurantIdCannotBeNullOrNegativeException("Restaurant ID cannot be null and must be greater than zero"))
                .when(orderService).createOrder(any(), any(), any(), any());

        mockMvc.perform(MockMvcRequestBuilders.post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Bad Request: Restaurant ID cannot be null and must be greater than zero"));

        verify(orderService, times(1)).createOrder(any(), any(), any(), any());
    }

    @Test
    void testCreateOrderWithNegativeRestaurantId() throws Exception {
        OrderItem item1 = new OrderItem(1L, "Pizza", 199.0, 2);
        List<OrderItem> orderItems = Collections.singletonList(item1);
        OrderDto orderDto = new OrderDto(-1L, 1L, "Nizampet, Hyderabad", orderItems);
        String jsonRequestBody = objectMapper.writeValueAsString(orderDto);

        doThrow(new RestaurantIdCannotBeNullOrNegativeException("Restaurant ID cannot be null and must be greater than zero"))
                .when(orderService).createOrder(any(), any(), any(), any());

        mockMvc.perform(MockMvcRequestBuilders.post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Bad Request: Restaurant ID cannot be null and must be greater than zero"));

        verify(orderService, times(1)).createOrder(any(), any(), any(), any());
    }

    @Test
    void testCreateOrderWithNullCustomerId() throws Exception {
        OrderItem item1 = new OrderItem(1L, "Pizza", 199.0, 2);
        List<OrderItem> orderItems = Collections.singletonList(item1);
        OrderDto orderDto = new OrderDto(1L, null, "Nizampet, Hyderabad", orderItems);
        String jsonRequestBody = objectMapper.writeValueAsString(orderDto);

        doThrow(new CustomerIdCannotBeNullOrNegativeException("Customer ID cannot be null and must be greater than zero"))
                .when(orderService).createOrder(any(), any(), any(), any());

        mockMvc.perform(MockMvcRequestBuilders.post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Bad Request: Customer ID cannot be null and must be greater than zero"));

        verify(orderService, times(1)).createOrder(any(), any(), any(), any());
    }

    @Test
    void testCreateOrderWithNegativeCustomerId() throws Exception {
        OrderItem item1 = new OrderItem(1L, "Pizza", 199.0, 2);
        List<OrderItem> orderItems = Collections.singletonList(item1);
        OrderDto orderDto = new OrderDto(1L, -1L, "Nizampet, Hyderabad", orderItems);
        String jsonRequestBody = objectMapper.writeValueAsString(orderDto);

        doThrow(new CustomerIdCannotBeNullOrNegativeException("Customer ID cannot be null and must be greater than zero"))
                .when(orderService).createOrder(any(), any(), any(), any());

        mockMvc.perform(MockMvcRequestBuilders.post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Bad Request: Customer ID cannot be null and must be greater than zero"));

        verify(orderService, times(1)).createOrder(any(), any(), any(), any());
    }

    @Test
    void testCreateOrderWithNullDeliveryAddress() throws Exception {
        OrderItem item1 = new OrderItem(1L, "Pizza", 199.0, 2);
        List<OrderItem> orderItems = Collections.singletonList(item1);
        OrderDto orderDto = new OrderDto(1L, 1L, null, orderItems);
        String jsonRequestBody = objectMapper.writeValueAsString(orderDto);

        doThrow(new DeliveryAddressCannotBeNullOrEmpty("Delivery address cannot be null or empty"))
                .when(orderService).createOrder(any(), any(), any(), any());

        mockMvc.perform(MockMvcRequestBuilders.post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Bad Request: Delivery address cannot be null or empty"));

        verify(orderService, times(1)).createOrder(any(), any(), any(), any());
    }

    @Test
    void testCreateOrderWithEmptyDeliveryAddress() throws Exception {
        OrderItem item1 = new OrderItem(1L, "Pizza", 199.0, 2);
        List<OrderItem> orderItems = Collections.singletonList(item1);
        OrderDto orderDto = new OrderDto(1L, 1L, "", orderItems);
        String jsonRequestBody = objectMapper.writeValueAsString(orderDto);

        doThrow(new DeliveryAddressCannotBeNullOrEmpty("Delivery address cannot be null or empty"))
                .when(orderService).createOrder(any(), any(), any(), any());

        mockMvc.perform(MockMvcRequestBuilders.post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Bad Request: Delivery address cannot be null or empty"));

        verify(orderService, times(1)).createOrder(any(), any(), any(), any());
    }


    @Test
    void testCreateOrderWithNullItems() throws Exception {
        OrderDto orderDto = new OrderDto(1L, 1L, "Nizampet, Hyderabad", null);
        String jsonRequestBody = objectMapper.writeValueAsString(orderDto);

        doThrow(new OrderItemsCannotBeNullOrEmptyException("Order items cannot be null or empty"))
                .when(orderService).createOrder(any(), any(), any(), any());

        mockMvc.perform(MockMvcRequestBuilders.post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Bad Request: Order items cannot be null or empty"));

        verify(orderService, times(1)).createOrder(any(), any(), any(), any());
    }

    @Test
    void testCreateOrderWithEmptyItems() throws Exception {
        OrderDto orderDto = new OrderDto(1L, 1L, "Nizampet, Hyderabad", Collections.emptyList());
        String jsonRequestBody = objectMapper.writeValueAsString(orderDto);

        doThrow(new OrderItemsCannotBeNullOrEmptyException("Order items cannot be null or empty"))
                .when(orderService).createOrder(any(), any(), any(), any());

        mockMvc.perform(MockMvcRequestBuilders.post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Bad Request: Order items cannot be null or empty"));

        verify(orderService, times(1)).createOrder(any(), any(), any(), any());
    }

    @Test
    void testCreateOrderWithNullMenuItemId() throws Exception {
        String jsonRequestBody = """
                {
                   "restaurantId": 3,
                   "deliveryAddress": "Nizampet, Hyderabad",
                   "customerId": 1,
                   "orderItems": [
                     {
                       "menuItemId": null,
                       "quantity": 5
                     }
                   ]
                 }
        """;

        doThrow(new MenuItemIdCannotBeNullOrNegativeException("Menu item ID cannot be null and must be greater than zero"))
                .when(orderService).createOrder(any(), any(), any(), any());

        mockMvc.perform(MockMvcRequestBuilders.post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Bad Request: Menu item ID cannot be null and must be greater than zero"));

        verify(orderService, times(1)).createOrder(any(), any(), any(), any());
    }

    @Test
    void testCreateOrderWithNegativeMenuItemId() throws Exception {
        String jsonRequestBody = """
                {
                   "restaurantId": 3,
                   "customerId": 1,
                   "deliveryAddress": "Nizampet, Hyderabad",
                   "orderItems": [
                     {
                       "menuItemId": -1,
                       "quantity": 5
                     }
                   ]
                 }
        """;

        doThrow(new MenuItemIdCannotBeNullOrNegativeException("Menu item ID cannot be null and must be greater than zero"))
                .when(orderService).createOrder(any(), any(), any(), any());

        mockMvc.perform(MockMvcRequestBuilders.post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Bad Request: Menu item ID cannot be null and must be greater than zero"));

        verify(orderService, times(1)).createOrder(any(), any(), any(), any());
    }

    @Test
    void testCreateOrderWithZeroQuantity() throws Exception {
        String jsonRequestBody = """
            {
               "restaurantId": 3,
               "customerId": 1,
               "deliveryAddress": "Nizampet, Hyderabad",
               "orderItems": [
                 {
                   "menuItemId": 1,
                   "quantity": 0
                 }
               ]
             }
        """;

        doThrow(new QuantityCannotBeNullOrNegativeException("Quantity cannot be null and must be greater than zero"))
                .when(orderService).createOrder(any(), any(), any(), any());

        mockMvc.perform(MockMvcRequestBuilders.post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Bad Request: Quantity cannot be null and must be greater than zero"));

        verify(orderService, times(1)).createOrder(any(), any(), any(), any());
    }

    @Test
    void testCreateOrderWithNegativeQuantity() throws Exception {
        String jsonRequestBody = """
            {
               "restaurantId": 3,
               "customerId": 1,
               "deliveryAddress": "Nizampet, Hyderabad",
               "orderItems": [
                 {
                   "menuItemId": 1,
                   "quantity": -5
                 }
               ]
             }
        """;

        doThrow(new QuantityCannotBeNullOrNegativeException("Quantity cannot be null and must be greater than zero"))
                .when(orderService).createOrder(any(), any(), any(), any());

        mockMvc.perform(MockMvcRequestBuilders.post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Bad Request: Quantity cannot be null and must be greater than zero"));

        verify(orderService, times(1)).createOrder(any(), any(), any(), any());
    }

    @Test
    void testCreateOrderWhenMenuItemDoesNotExist() throws Exception {
        String jsonRequestBody = """
            {
               "restaurantId": 1,
               "customerId": 1,
               "deliveryAddress": "Nizampet, Hyderabad",
               "orderItems": [
                 {
                   "menuItemId": 10,
                   "quantity": 5
                 }
               ]
             }
        """;

        doThrow(new MenuItemNotFoundException("Menu item with restaurant id: 1 and menu item id: 10 is not found"))
                .when(orderService).createOrder(any(), any(), any(), any());

        mockMvc.perform(MockMvcRequestBuilders.post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestBody))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Not Found: Menu item with restaurant id: 1 and menu item id: 10 is not found"));

        verify(orderService, times(1)).createOrder(any(), any(), any(), any());
    }

    @Test
    void testGetAllOrders() throws Exception {
        OrderItem item1 = new OrderItem(1L, "Pizza", 199.0, 2);
        List<OrderItem> orderItems = Collections.singletonList(item1);

        Order order1 = new Order(1L, 1L, "Nizampet, Hyderabad", orderItems);
        Order order2 = new Order(2L, 1L, "Nizampet, Hyderabad", orderItems);
        List<Order> expectedOrders = Arrays.asList(order1, order2);
        String expectedResponseBody = objectMapper.writeValueAsString(expectedOrders);

        when(orderService.getAllOrders()).thenReturn(expectedOrders);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/orders")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString();
        assertEquals(expectedResponseBody, responseBody);
        verify(orderService, times(1)).getAllOrders();
    }

    @Test
    void testGetOrdersWhenNoAllOrders() throws Exception {
        when(orderService.getAllOrders()).thenReturn(Collections.emptyList());

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/orders")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString();
        assertEquals("[]", responseBody);
        verify(orderService, times(1)).getAllOrders();
    }

    @Test
    void testGetOrderByIdSuccessfully() throws Exception {
        OrderItem item1 = new OrderItem(1L, "Pizza", 199.0, 2);
        List<OrderItem> orderItems = Collections.singletonList(item1);
        Order expectedOrder = new Order(1L, 1L, "Nizampet, Hyderabad", orderItems);
        String expectedResponseBody = objectMapper.writeValueAsString(expectedOrder);

        when(orderService.getOrderById(1L)).thenReturn(expectedOrder);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/orders/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString();
        assertEquals(expectedResponseBody, responseBody);
        verify(orderService, times(1)).getOrderById(1L);
    }

    @Test
    void testGetOrderByIdWhenOrderNotFound() throws Exception {
        when(orderService.getOrderById(99L)).thenThrow(new OrderNotFoundException("Order not found with id: 99"));

        mockMvc.perform(MockMvcRequestBuilders.get("/orders/99")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Not Found: Order not found with id: 99"));

        verify(orderService, times(1)).getOrderById(99L);
    }

    @Test
    void testUpdateOrderStatusSuccessfully() throws Exception {
        Long orderId = 1L;
        OrderItem item1 = new OrderItem(1L, "Pizza", 199.0, 2);
        List<OrderItem> orderItems = Collections.singletonList(item1);
        Order order = new Order(orderId, 1L, "Nizampet, Hyderabad", orderItems);

        when(orderService.updateOrderStatus(orderId)).thenReturn(order);

        mockMvc.perform(MockMvcRequestBuilders.put("/orders/" + orderId + "/status")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Order status updated to OUT FOR DELIVERY for order ID: " + orderId));

        verify(orderService, times(1)).updateOrderStatus(orderId);
    }

    @Test
    void testUpdateOrderStatusWhenOrderNotFound() throws Exception {
        Long orderId = 99L;

        doThrow(new OrderNotFoundException("Order not found with id: " + orderId))
                .when(orderService).updateOrderStatus(orderId);

        mockMvc.perform(MockMvcRequestBuilders.put("/orders/" + orderId + "/status")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Not Found: Order not found with id: " + orderId));

        verify(orderService, times(1)).updateOrderStatus(orderId);
    }

    @Test
    void testUpdateOrderStatusWhenServerErrorOccurs() throws Exception {
        Long orderId = 1L;

        doThrow(new RuntimeException("Unexpected error"))
                .when(orderService).updateOrderStatus(orderId);

        mockMvc.perform(MockMvcRequestBuilders.put("/orders/" + orderId + "/status")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("An error occurred: Unexpected error"));

        verify(orderService, times(1)).updateOrderStatus(orderId);
    }
}
