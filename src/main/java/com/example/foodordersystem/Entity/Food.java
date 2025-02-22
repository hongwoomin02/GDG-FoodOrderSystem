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
    //mappedby: orderitem의 food 필드에 의해 매핑 cascade: food 엔티티 변경-> orderitem 영향
    // orphan -> food 삭제 orderitem 자동삭제

    @OneToMany(mappedBy = "food", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    protected Food() {}

    public Food(String name, int price) {
        this.name = name;
        this.price = price;
    }
}
