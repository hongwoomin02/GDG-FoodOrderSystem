package com.example.foodordersystem.Entity;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "food")
public class Food {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private int price;

    @ManyToMany(mappedBy = "foods")
    private List<Order> orders = new ArrayList<>();

    protected Food() {}

    public Food(String name, int price) {
        this.name = name;
        this.price = price;
    }
}
