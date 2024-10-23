package com.example.order.service;

import com.example.order.dto.MenuItemDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Service
public class CatalogClientService {
    private static final String BASE_URL = "http://localhost:8080/catalog/restaurants";

    public MenuItemDto getMenuItemByIdAndRestaurantId(Long restaurantId, Long menuItemId) {
        RestTemplate restTemplate = new RestTemplate();
        String url = BASE_URL + "/" + restaurantId + "/menuItems/" + menuItemId;
        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });

        String rawResponse = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        MenuItemDto menuItem;

        try {
            menuItem = objectMapper.readValue(rawResponse, MenuItemDto.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse menu item response", e);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        return menuItem;
    }
}
