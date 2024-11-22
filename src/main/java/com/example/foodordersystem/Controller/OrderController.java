package com.example.foodordersystem.Controller;

import com.example.foodordersystem.DTO.Order.AddOrderRequestDTO;
import com.example.foodordersystem.DTO.Order.EditOrderRequestDTO;
import com.example.foodordersystem.DTO.Order.OrderResponseDTO;
import com.example.foodordersystem.Service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
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

    @PatchMapping
    public void editOrder(@RequestBody EditOrderRequestDTO editOrderDTO) {
        orderService.editOrder(editOrderDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
    }
}
