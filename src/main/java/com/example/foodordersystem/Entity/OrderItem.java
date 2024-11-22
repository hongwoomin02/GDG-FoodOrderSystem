package com.example.foodordersystem.Entity;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "order_item")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "food_id", nullable = false)
    private Food food;

    private int quantity;

    protected OrderItem() {}

    public OrderItem(Order order, Food food, int quantity) {
        this.order = order;
        this.food = food;
        this.quantity = quantity;
    }
    public void setOrder(Order order) {
        this.order = order;
    }
}
