package com.example.foodordersystem.DTO.Order;

import java.util.List;

public record AddOrderRequestDTO(Long userId, List<FoodRequestDTO> foodOrders){
    public record FoodRequestDTO(Long foodId, int quantity) {}
}
