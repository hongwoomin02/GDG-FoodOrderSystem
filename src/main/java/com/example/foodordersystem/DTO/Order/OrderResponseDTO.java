package com.example.foodordersystem.DTO.Order;

import java.util.List;

public record OrderResponseDTO(Long orderId, String userName, List<FoodDTO> foodDTO){}
