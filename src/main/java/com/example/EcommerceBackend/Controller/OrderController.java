package com.example.EcommerceBackend.Controller;

import com.example.EcommerceBackend.DTO.OrderDto;
import com.example.EcommerceBackend.Service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/create/{userId}")
    public ResponseEntity<OrderDto> createOrderFromCart(@PathVariable int userId) {
        OrderDto order = orderService.createOrderFromCart(userId);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }
    @GetMapping("/{userId}")
    public ResponseEntity<List<OrderDto>> getAllOrdersByUserId(@PathVariable int userId) {
        List<OrderDto> orders = orderService.getOrdersByUserId(userId);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @DeleteMapping("/cancel/{orderId}/{userId}")
    public ResponseEntity<String> cancelOrder(@PathVariable int orderId, @PathVariable int userId) {
        String message = orderService.cancelOrder(orderId, userId);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
