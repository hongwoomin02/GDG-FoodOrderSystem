package com.example.foodordersystem.Repository;

import com.example.foodordersystem.Entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,Long>,CustomOrderRepository{


}
