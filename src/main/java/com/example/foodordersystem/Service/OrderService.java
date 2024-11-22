package com.example.foodordersystem.Service;

import com.example.foodordersystem.DTO.Order.AddOrderRequestDTO;
import com.example.foodordersystem.DTO.Order.EditOrderRequestDTO;
import com.example.foodordersystem.DTO.Order.FoodDTO;
import com.example.foodordersystem.DTO.Order.OrderResponseDTO;
import com.example.foodordersystem.Entity.Food;
import com.example.foodordersystem.Entity.Order;
import com.example.foodordersystem.Entity.OrderItem;
import com.example.foodordersystem.Entity.User;
import com.example.foodordersystem.Repository.FoodRepository;
import com.example.foodordersystem.Repository.OrderRepository;
import com.example.foodordersystem.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final FoodRepository foodRepository;
    private final UserRepository userRepository;

    public OrderService(OrderRepository orderRepository, FoodRepository foodRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.foodRepository = foodRepository;
        this.userRepository = userRepository;
    }

    public List<OrderResponseDTO> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        List<OrderResponseDTO> allOrders = new ArrayList<>();

        for (Order order : orders) {
            List<FoodDTO> foodDTOList = new ArrayList<>();
            for (OrderItem item : order.getOrderItems()) {
                foodDTOList.add(new FoodDTO(item.getFood().getId(), item.getCount()));
            }

            allOrders.add(new OrderResponseDTO(order.getId(), order.getUser().getName(), foodDTOList));
        }

        return allOrders;
    }

    @Transactional
    public void addOrder(AddOrderRequestDTO orderDTO) {
        User user = userRepository.findById(orderDTO.userId()).orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        if (orderDTO.foodDTO() == null || orderDTO.foodDTO().isEmpty()) {
            throw new IllegalArgumentException("음식 수량 목록이 비어 있습니다.");
        }
        Order order = new Order(user);

        for (FoodDTO foodData : orderDTO.foodDTO()) {
            Food food = foodRepository.findById(foodData.foodId()).orElseThrow(() -> new IllegalArgumentException("음식을 찾을 수 없습니다."));
            OrderItem orderItem = new OrderItem(food, foodData.count());
            order.addOrderItem(orderItem);
        }

        orderRepository.save(order);
    }

    public void editOrder(EditOrderRequestDTO orderDTO) {
        Order order = orderRepository.findById(orderDTO.id()).orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다."));

        if (orderDTO.foodDTO() == null || orderDTO.foodDTO().isEmpty()) {
            throw new IllegalArgumentException("음식 수량 목록이 비어 있습니다.");
        }

        order.getOrderItems().clear();

        for (FoodDTO foodData : orderDTO.foodDTO()) {
            Food food = foodRepository.findById(foodData.foodId()).orElseThrow(() -> new IllegalArgumentException("음식을 찾을 수 없습니다."));
            OrderItem orderItem = new OrderItem(food, foodData.count());
            order.addOrderItem(orderItem);
        }
    }

    @Transactional
    public void deleteOrder(Long id) {
        orderRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다."));
        orderRepository.deleteById(id);
    }
}
