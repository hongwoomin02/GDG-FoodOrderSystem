package com.example.foodordersystem.Repository;

import com.example.foodordersystem.Entity.Food;
import com.example.foodordersystem.Entity.Order;
import com.example.foodordersystem.Entity.OrderItem;
import com.example.foodordersystem.Entity.User;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
public class OrderRepositoryImplTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void testFindAllOrders() {
        // Given
        User user1 = new User("고양이", "cat@google.com", "meow");
        User user2 = new User("강아지", "dog@google.com", "bark");
        userRepository.saveAll(List.of(user1, user2));

        Food food1 = new Food("치킨", 20000);
        Food food2 = new Food("피자", 30000);
        foodRepository.saveAll(List.of(food1, food2));

        Order order1 = new Order(user1);
        OrderItem item1 = new OrderItem(order1, food1, 1);
        order1.addOrderItem(item1);

        Order order2 = new Order(user2);
        OrderItem item2 = new OrderItem(order2, food2, 2);
        order2.addOrderItem(item2);

        orderRepository.saveAll(List.of(order1, order2));

        // When
        List<Order> orders = orderRepository.findAllOrders();

        // Then
        assertThat(orders.get(0).getUser().getEmail()).isEqualTo(user1.getEmail());
        assertThat(orders.get(0).getOrderItems()).hasSize(1);
        assertThat(orders.get(0).getOrderItems().get(0).getFood().getName()).isEqualTo(food1.getName());
        assertThat(orders.get(0).getOrderItems().get(0).getQuantity()).isEqualTo(1);

        // 두 번째 주문 확인
        assertThat(orders.get(1).getUser().getEmail()).isEqualTo(user2.getEmail());
        assertThat(orders.get(1).getOrderItems()).hasSize(1);
        assertThat(orders.get(1).getOrderItems().get(0).getFood().getName()).isEqualTo(food2.getName());
        assertThat(orders.get(1).getOrderItems().get(0).getQuantity()).isEqualTo(2);
    }


    @Test
    void testFindFoodById() {
        // Given
        Food food1 = new Food("치킨", 20000);
        Food food2 = new Food("피자", 30000);
        foodRepository.saveAll(List.of(food1, food2));

        // When
        List<Food> foods = orderRepository.findFoodById(List.of(food1.getId(), food2.getId()));

        // Then
        Food Food1 = foods.get(0);
        assertThat(Food1.getName()).isEqualTo("치킨");
        assertThat(Food1.getPrice()).isEqualTo(20000);

        Food Food2 = foods.get(1);
        assertThat(Food2.getName()).isEqualTo("피자");
        assertThat(Food2.getPrice()).isEqualTo(30000);
    }

    @Test
    void testDeleteOrderById() {
        // Given
        User user = new User("h", "h", "h");
        userRepository.save(user);

        Food food1 = new Food("피자", 30000);
        foodRepository.save(food1);

        Order order = new Order(user);
        OrderItem item = new OrderItem(order, food1, 2);
        order.addOrderItem(item);

        orderRepository.save(order);
        Long orderId = order.getId();

        // When
        orderRepository.deleteOrderById(orderId);
        entityManager.flush();
        entityManager.clear();

        // Then
        assertThat(orderRepository.findById(orderId)).isEmpty();
    }
}
