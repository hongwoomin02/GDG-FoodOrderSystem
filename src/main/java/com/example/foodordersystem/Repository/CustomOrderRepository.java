package com.example.foodordersystem.Repository;

import com.example.foodordersystem.Entity.Order;

import java.util.List;

public interface CustomOrderRepository {
    List<Order> findAllOrders();
}
