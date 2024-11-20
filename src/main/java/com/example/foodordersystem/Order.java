package com.example.foodordersystem;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "`order`")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int count;

    protected  Order() {};

    public Order(String name, int count) {
        this.name = name;
        this.count = count;

    }

    public void setCount(int count) {
        this.count = count;
    }

}
