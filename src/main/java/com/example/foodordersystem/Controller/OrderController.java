package com.example.foodordersystem.Controller;

import com.example.foodordersystem.AddOrderDTO;
import com.example.foodordersystem.EditOrderDTO;
import com.example.foodordersystem.OrderDTO;
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
    public List<OrderDTO> getAllOrders() {
        return orderService.getAllOrders();
    }

    @PostMapping
    public void addOrder(@RequestBody AddOrderDTO addOrderDTO) {
        orderService.addOrder(addOrderDTO);
    }

    @PatchMapping("/{id}")
    public void editOrder(@PathVariable Long id, @RequestBody EditOrderDTO editOrderDTO) {
        orderService.editOrder(editOrderDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
    }
}
