package com.example.foodordersystem;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "`order`")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int count;



    public Order(){}

    public Order(String name, int count) {
        this.name = name;
        this.count = count;

    }
}
