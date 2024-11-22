package com.example.foodordersystem.Controller;

import com.example.foodordersystem.DTO.Order.AddOrderRequestDTO;
import com.example.foodordersystem.DTO.Order.EditOrderRequestDTO;
import com.example.foodordersystem.DTO.Order.OrderResponseDTO;
import com.example.foodordersystem.Service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public List<OrderResponseDTO> getAllOrders() {
        return orderService.getAllOrders();
    }

    @PostMapping
    public void addOrder(@RequestBody AddOrderRequestDTO addOrderDTO) {
        orderService.addOrder(addOrderDTO);
    }

    @PatchMapping("/{id}") //PathVariable이랑 Requestbzody 둘중 하나만 써도 될텐데,,
    public void editOrder(@PathVariable Long id, @RequestBody EditOrderRequestDTO editOrderDTO) {
        orderService.editOrder(editOrderDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
    }
}
