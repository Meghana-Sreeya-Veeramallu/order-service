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
        OrderDto orderDto = new OrderDto(1L, 1L, orderItems);
        String jsonRequestBody = objectMapper.writeValueAsString(orderDto);

        Order createdOrder = new Order(1L, 1L, orderItems);
        when(orderService.createOrder(any(OrderDto.class))).thenReturn(createdOrder);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestBody))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString();
        assertEquals("Order created successfully with total price 497.0", responseBody);
        verify(orderService, times(1)).createOrder(any(OrderDto.class));
    }

    @Test
    void testCreateOrderWithNullRestaurantId() throws Exception {
        OrderItem item1 = new OrderItem(1L, "Pizza", 199.0, 2);
        OrderItem item2 = new OrderItem(2L, "Burger", 99.0, 1);
        List<OrderItem> orderItems = Arrays.asList(item1, item2);
        OrderDto orderDto = new OrderDto(null, 1L, orderItems);
        String jsonRequestBody = objectMapper.writeValueAsString(orderDto);

        doThrow(new RestaurantIdCannotBeNullOrNegativeException("Restaurant ID cannot be null and must be greater than zero"))
                .when(orderService).createOrder(any(OrderDto.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Bad Request: Restaurant ID cannot be null and must be greater than zero"));

        verify(orderService, times(1)).createOrder(any());
    }

    @Test
    void testCreateOrderWithNegativeRestaurantId() throws Exception {
        OrderItem item1 = new OrderItem(1L, "Pizza", 199.0, 2);
        List<OrderItem> orderItems = Collections.singletonList(item1);
        OrderDto orderDto = new OrderDto(-1L, 1L, orderItems);
        String jsonRequestBody = objectMapper.writeValueAsString(orderDto);

        doThrow(new RestaurantIdCannotBeNullOrNegativeException("Restaurant ID cannot be null and must be greater than zero"))
                .when(orderService).createOrder(any(OrderDto.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Bad Request: Restaurant ID cannot be null and must be greater than zero"));

        verify(orderService, times(1)).createOrder(any());
    }

    @Test
    void testCreateOrderWithNullCustomerId() throws Exception {
        OrderItem item1 = new OrderItem(1L, "Pizza", 199.0, 2);
        List<OrderItem> orderItems = Collections.singletonList(item1);
        OrderDto orderDto = new OrderDto(1L, null, orderItems);
        String jsonRequestBody = objectMapper.writeValueAsString(orderDto);

        doThrow(new CustomerIdCannotBeNullOrNegativeException("Customer ID cannot be null and must be greater than zero"))
                .when(orderService).createOrder(any(OrderDto.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Bad Request: Customer ID cannot be null and must be greater than zero"));

        verify(orderService, times(1)).createOrder(any());
    }

    @Test
    void testCreateOrderWithNegativeCustomerId() throws Exception {
        OrderItem item1 = new OrderItem(1L, "Pizza", 199.0, 2);
        List<OrderItem> orderItems = Collections.singletonList(item1);
        OrderDto orderDto = new OrderDto(1L, -1L, orderItems);
        String jsonRequestBody = objectMapper.writeValueAsString(orderDto);

        doThrow(new CustomerIdCannotBeNullOrNegativeException("Customer ID cannot be null and must be greater than zero"))
                .when(orderService).createOrder(any(OrderDto.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Bad Request: Customer ID cannot be null and must be greater than zero"));

        verify(orderService, times(1)).createOrder(any());
    }

    @Test
    void testCreateOrderWithNullItems() throws Exception {
        OrderDto orderDto = new OrderDto(1L, 1L, null);
        String jsonRequestBody = objectMapper.writeValueAsString(orderDto);

        doThrow(new OrderItemsCannotBeNullOrEmptyException("Order items cannot be null or empty"))
                .when(orderService).createOrder(any(OrderDto.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Bad Request: Order items cannot be null or empty"));

        verify(orderService, times(1)).createOrder(any());
    }

    @Test
    void testCreateOrderWithEmptyItems() throws Exception {
        OrderDto orderDto = new OrderDto(1L, 1L, Collections.emptyList());
        String jsonRequestBody = objectMapper.writeValueAsString(orderDto);

        doThrow(new OrderItemsCannotBeNullOrEmptyException("Order items cannot be null or empty"))
                .when(orderService).createOrder(any(OrderDto.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Bad Request: Order items cannot be null or empty"));

        verify(orderService, times(1)).createOrder(any());
    }

    @Test
    void testCreateOrderWithNullMenuItemId() throws Exception {
        String jsonRequestBody = """
                {
                   "restaurantId": 3,
                   "customerId": 1,
                   "orderItems": [
                     {
                       "menuItemId": null,
                       "menuItemName": "Choco Lava cake",
                       "price": 249,
                       "quantity": 5
                     }
                   ]
                 }
        """;

        doThrow(new MenuItemIdCannotBeNullOrNegativeException("Menu item ID cannot be null and must be greater than zero"))
                .when(orderService).createOrder(any(OrderDto.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Bad Request: Menu item ID cannot be null and must be greater than zero"));

        verify(orderService, times(1)).createOrder(any());
    }

    @Test
    void testCreateOrderWithNegativeMenuItemId() throws Exception {
        String jsonRequestBody = """
                {
                   "restaurantId": 3,
                   "customerId": 1,
                   "orderItems": [
                     {
                       "menuItemId": -1,
                       "menuItemName": "Choco Lava cake",
                       "price": 249,
                       "quantity": 5
                     }
                   ]
                 }
        """;

        doThrow(new MenuItemIdCannotBeNullOrNegativeException("Menu item ID cannot be null and must be greater than zero"))
                .when(orderService).createOrder(any(OrderDto.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Bad Request: Menu item ID cannot be null and must be greater than zero"));

        verify(orderService, times(1)).createOrder(any());
    }

    @Test
    void testCreateOrderWithNullMenuItemName() throws Exception {
        String jsonRequestBody = """
                {
                   "restaurantId": 3,
                   "customerId": 1,
                   "orderItems": [
                     {
                       "menuItemId": 1,
                       "menuItemName": null,
                       "price": 249,
                       "quantity": 5
                     }
                   ]
                 }
        """;

        doThrow(new MenuItemNameCannotBeNullOrEmptyException("Menu item name cannot be null or empty"))
                .when(orderService).createOrder(any(OrderDto.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Bad Request: Menu item name cannot be null or empty"));

        verify(orderService, times(1)).createOrder(any());
    }

    @Test
    void testCreateOrderWithEmptyMenuItemName() throws Exception {
        String jsonRequestBody = """
                {
                   "restaurantId": 3,
                   "customerId": 1,
                   "orderItems": [
                     {
                       "menuItemId": 1,
                       "menuItemName": " ",
                       "price": 249,
                       "quantity": 5
                     }
                   ]
                 }
        """;

        doThrow(new MenuItemNameCannotBeNullOrEmptyException("Menu item name cannot be null or empty"))
                .when(orderService).createOrder(any(OrderDto.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Bad Request: Menu item name cannot be null or empty"));

        verify(orderService, times(1)).createOrder(any());
    }

    @Test
    void testCreateOrderWithZeroPrice() throws Exception {
        String jsonRequestBody = """
            {
               "restaurantId": 3,
               "customerId": 1,
               "orderItems": [
                 {
                   "menuItemId": 1,
                   "menuItemName": "Choco Lava cake",
                   "price": 0,
                   "quantity": 5
                 }
               ]
             }
        """;

        doThrow(new PriceCannotBeNullOrNegativeException("Price cannot be null and must be greater than zero"))
                .when(orderService).createOrder(any(OrderDto.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Bad Request: Price cannot be null and must be greater than zero"));

        verify(orderService, times(1)).createOrder(any(OrderDto.class));
    }

    @Test
    void testCreateOrderWithNegativePrice() throws Exception {
        String jsonRequestBody = """
            {
               "restaurantId": 3,
               "customerId": 1,
               "orderItems": [
                 {
                   "menuItemId": 1,
                   "menuItemName": "Choco Lava cake",
                   "price": -10,
                   "quantity": 5
                 }
               ]
             }
        """;

        doThrow(new PriceCannotBeNullOrNegativeException("Price cannot be null and must be greater than zero"))
                .when(orderService).createOrder(any(OrderDto.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Bad Request: Price cannot be null and must be greater than zero"));

        verify(orderService, times(1)).createOrder(any(OrderDto.class));
    }

    @Test
    void testCreateOrderWithZeroQuantity() throws Exception {
        String jsonRequestBody = """
            {
               "restaurantId": 3,
               "customerId": 1,
               "orderItems": [
                 {
                   "menuItemId": 1,
                   "menuItemName": "Choco Lava cake",
                   "price": 249,
                   "quantity": 0
                 }
               ]
             }
        """;

        doThrow(new QuantityCannotBeNullOrNegativeException("Quantity cannot be null and must be greater than zero"))
                .when(orderService).createOrder(any(OrderDto.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Bad Request: Quantity cannot be null and must be greater than zero"));

        verify(orderService, times(1)).createOrder(any(OrderDto.class));
    }

    @Test
    void testCreateOrderWithNegativeQuantity() throws Exception {
        String jsonRequestBody = """
            {
               "restaurantId": 3,
               "customerId": 1,
               "orderItems": [
                 {
                   "menuItemId": 1,
                   "menuItemName": "Choco Lava cake",
                   "price": 249,
                   "quantity": -5
                 }
               ]
             }
        """;

        doThrow(new QuantityCannotBeNullOrNegativeException("Quantity cannot be null and must be greater than zero"))
                .when(orderService).createOrder(any(OrderDto.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Bad Request: Quantity cannot be null and must be greater than zero"));

        verify(orderService, times(1)).createOrder(any(OrderDto.class));
    }
}
