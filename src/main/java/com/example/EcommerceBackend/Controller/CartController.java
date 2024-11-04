package com.example.EcommerceBackend.Controller;

import com.example.EcommerceBackend.DTO.CartDto;
import com.example.EcommerceBackend.Service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/{userId}/add/{productId}")
    public void addProductToCart(@PathVariable int userId, @PathVariable int productId, @RequestParam int quantity) {
        cartService.addProductToCart(userId, productId, quantity);
    }

    @GetMapping("/{userId}")
    public CartDto getCart(@PathVariable int userId) {
        return cartService.getCart(userId);
    }

    @DeleteMapping("/{userId}/clear")
    public void clearCart(@PathVariable int userId) {
        cartService.clearCart(userId);
    }
}

