package com.example.foodordersystem;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Controller
public class OrderController {
    private final OrderService orderSerivce;

    public OrderController(OrderService orderSerivce) {
        this.orderSerivce = orderSerivce;
    }
    @GetMapping("/menu")
    public String getMenu(){
        return "menu";
    }
    @GetMapping("/order")
    public String getAllOrders(Model model){
        List<OrderDTO> orders = orderSerivce.getAllOrders();
        model.addAttribute("orders", orders);
        return "orderList";
    }
    @PostMapping("/add")
    public String addOrder(@RequestParam String name, @RequestParam int count) {
        orderSerivce.addOrder(new AddOrderDTO(name, count));
        return "redirect:/order";
    }

    @PostMapping("/edit")
    public String editOrder(@RequestParam Long id, @RequestParam int count) {
        orderSerivce.editOrder(new EditOrderDTO(id, count));
        return "redirect:/order";
    }
    @PostMapping("/delete")
    public String deleteOrder(@RequestParam Long id) {
        orderSerivce.deleteOrder(id);
        return "redirect:/order";
    }

}
