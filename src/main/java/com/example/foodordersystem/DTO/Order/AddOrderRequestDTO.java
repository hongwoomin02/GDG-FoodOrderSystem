package com.example.foodordersystem.DTO.Order;

import java.util.List;

public record AddOrderRequestDTO(Long userId, List<Long> foodIds){}
