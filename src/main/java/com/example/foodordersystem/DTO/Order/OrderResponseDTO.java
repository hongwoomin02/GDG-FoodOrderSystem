package com.example.foodordersystem.DTO.Order;

import java.util.List;

public record OrderResponseDTO(Long id, String userName, List<FoodResponseDTO> foods) {}

