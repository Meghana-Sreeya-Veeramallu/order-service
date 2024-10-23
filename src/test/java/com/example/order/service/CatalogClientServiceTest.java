package com.example.order.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.example.order.dto.MenuItemDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class CatalogClientServiceTest {

    @InjectMocks
    private CatalogClientService catalogClientService;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetMenuItemByIdAndRestaurantIdSuccess() {
        Long restaurantId = 1L;
        Long menuItemId = 2L;
        String url = "http://localhost:8080/catalog/restaurants/" + restaurantId + "/menuItems/" + menuItemId;

        MenuItemDto expectedMenuItem = new MenuItemDto(menuItemId, "Farmhouse Pizza", 300.0);
        String jsonResponse = "{\"id\":2,\"name\":\"Farmhouse Pizza\",\"price\":300.0}";

        ResponseEntity<String> responseEntity = ResponseEntity.ok(jsonResponse);
        when(restTemplate.exchange(eq(url), eq(HttpMethod.GET), isNull(), any(ParameterizedTypeReference.class)))
                .thenReturn(responseEntity);

        MenuItemDto actualMenuItem = catalogClientService.getMenuItemByIdAndRestaurantId(restaurantId, menuItemId);

        assertEquals(expectedMenuItem.getId(), actualMenuItem.getId());
        assertEquals(expectedMenuItem.getName(), actualMenuItem.getName());
        assertEquals(expectedMenuItem.getPrice(), actualMenuItem.getPrice());
    }

    @Test
    void testGetMenuItemByIdAndRestaurantIdFailure() {
        Long restaurantId = 1L;
        Long menuItemId = 20L;
        String url = "http://localhost:8080/catalog/restaurants/" + restaurantId + "/menuItems/" + menuItemId;

        when(restTemplate.exchange(eq(url), eq(HttpMethod.GET), isNull(), any(ParameterizedTypeReference.class)))
                .thenThrow(new RuntimeException("404 : Not Found: Menu item with ID '20' not found for restaurant with ID '1'"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            catalogClientService.getMenuItemByIdAndRestaurantId(restaurantId, menuItemId);
        });

        String expectedMessage = "404 : Not Found: Menu item with ID '20' not found for restaurant with ID '1'";
        String actualMessage = exception.getMessage().replace("\"", "");
        assertEquals(expectedMessage, actualMessage);
    }
}
