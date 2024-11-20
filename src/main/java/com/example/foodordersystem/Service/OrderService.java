package com.example.foodordersystem.Service;

import com.example.foodordersystem.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<OrderDTO> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        List<OrderDTO> allOrders = new ArrayList<>();
        for (Order order : orders) {
            allOrders.add(new OrderDTO(order.getId(), order.getName(), order.getCount()));
        }
        return allOrders;
    }

    public void addOrder(AddOrderDTO orderDTO) {
        Order order = new Order(orderDTO.name(), orderDTO.count());
        orderRepository.save(order);
    }

    @Transactional
    public void editOrder(EditOrderDTO orderDTO) {
        Order order = orderRepository.findById(orderDTO.id())
                .orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다."));
        order.setCount(orderDTO.count());
    }

    public void deleteOrder(Long id) {
        orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다."));
        orderRepository.deleteById(id);
    }
}
