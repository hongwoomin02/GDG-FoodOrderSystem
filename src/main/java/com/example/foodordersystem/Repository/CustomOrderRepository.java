package com.example.foodordersystem.Repository;

import com.example.foodordersystem.Entity.Food;
import com.example.foodordersystem.Entity.Order;
import com.example.foodordersystem.Entity.OrderItem;

import java.util.List;

public interface CustomOrderRepository {
    List<Order> findAllOrders();
    List<Food> findFoodById(List<Long> foodIds);
    void deleteOrderById(Long orderId);

}
