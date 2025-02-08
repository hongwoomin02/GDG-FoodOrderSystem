package com.example.foodordersystem.Service;

import com.example.foodordersystem.DTO.Order.AddOrderRequestDTO;
import com.example.foodordersystem.DTO.Order.EditOrderRequestDTO;
import com.example.foodordersystem.DTO.Order.FoodResponseDTO;
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
        List<Order> orders = orderRepository.findAllOrders();
        List<OrderResponseDTO> response = new ArrayList<>();

        for (Order order : orders) {
            List<FoodResponseDTO> foodDTOList = new ArrayList<>();
            for (OrderItem orderItem : order.getOrderItems()) {
                Food food = orderItem.getFood();
                int quantity = orderItem.getQuantity();
                foodDTOList.add(new FoodResponseDTO(food.getId(), food.getName(), food.getPrice(), quantity));
            }
            response.add(new OrderResponseDTO(order.getId(), order.getUser().getName(), foodDTOList));
        }
        return response;
    }


    @Transactional
    public void addOrder(AddOrderRequestDTO orderDTO) {
        User user = userRepository.findById(orderDTO.userId()).orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Order order = new Order(user);

        for (AddOrderRequestDTO.FoodRequestDTO foodOrderDTO : orderDTO.foodOrders()) {
            Food food = foodRepository.findById(foodOrderDTO.foodId()).orElseThrow(() -> new IllegalArgumentException("음식을 찾을 수 없습니다."));
            OrderItem orderItem = new OrderItem(order, food, foodOrderDTO.quantity());
            order.addOrderItem(orderItem);
        }

        orderRepository.save(order);
    }

    @Transactional
    public void editOrder(EditOrderRequestDTO orderDTO) {
        Order order = orderRepository.findById(orderDTO.userId()).orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다."));

        order.clearOrderItems();

        for (EditOrderRequestDTO.FoodRequestDTO foodOrderDTO : orderDTO.foodOrders()) {
            Food food = foodRepository.findById(foodOrderDTO.foodId()).orElseThrow(() -> new IllegalArgumentException("음식을 찾을 수 없습니다."));
            OrderItem orderItem = new OrderItem(order, food, foodOrderDTO.quantity());
            order.addOrderItem(orderItem);
        }


        orderRepository.save(order);
    }

    @Transactional
    public void deleteOrder(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new IllegalArgumentException("주문을 찾을 수 없습니다.");
        }
        orderRepository.deleteById(id);
    }
}
