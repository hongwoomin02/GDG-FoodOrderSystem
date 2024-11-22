package com.example.foodordersystem.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.foodordersystem.Entity.Food;

@Repository
public interface FoodRepository extends JpaRepository<Food, Long> {

}