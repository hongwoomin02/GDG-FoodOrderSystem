package com.example.foodordersystem.Entity;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "`order`")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToMany
    @JoinTable(
            name = "order_food",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "food_id")
    )
    private List<Food> foods = new ArrayList<>();

    protected Order() {}

    public Order(User user) {
        this.user = user;
    }

    public void addFood(Food food) {
        this.foods.add(food);
    }

    public void clearFoods() {
        this.foods.clear();
    }
}
