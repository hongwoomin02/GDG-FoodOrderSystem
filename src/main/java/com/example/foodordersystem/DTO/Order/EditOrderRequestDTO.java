package com.example.foodordersystem.DTO.Order;

import java.util.List;

public record EditOrderRequestDTO (Long id, List<Long> foodIds) {}
