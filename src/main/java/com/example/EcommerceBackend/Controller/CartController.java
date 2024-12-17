package com.example.EcommerceBackend.Controller;

import com.example.EcommerceBackend.DTO.CartDto;
import com.example.EcommerceBackend.DTO.CartItemDto;
import com.example.EcommerceBackend.Service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/{userId}/add-products")
    public Map<String, Object> addProductsToCart(@PathVariable int userId, @RequestBody List<CartItemDto> items) {
        return cartService.addProductsToCart(userId, items);
    }

    @GetMapping("/{userId}/cart")
    public Map<String, Object> getCart(@PathVariable int userId) {
        return cartService.getCartResponse(userId);
    }

    @DeleteMapping("/{userId}/clear")
    public void clearCart(@PathVariable int userId) {
        cartService.clearCart(userId);
    }
}

