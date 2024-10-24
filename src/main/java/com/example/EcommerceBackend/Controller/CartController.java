package com.example.EcommerceBackend.Controller;


import com.example.EcommerceBackend.DTO.CartDto;
import com.example.EcommerceBackend.Service.CartService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    // Add products to the user's cart
    @PostMapping("/add/{userId}") // Add userId as a path variable
    public ResponseEntity<CartDto> addProductsToCart(@PathVariable int userId, @RequestBody CartDto cartDto) {
        try {
            CartDto updatedCart = cartService.addProductsToUserCart(cartDto, userId); // Pass the userId to the service
            return ResponseEntity.ok(updatedCart);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }


    // Get the cart by user ID
    @GetMapping("/user/{userId}")
    public ResponseEntity<CartDto> getCartByUserId(@PathVariable Integer userId) {
        try {
            CartDto cartDto = cartService.getCartByUserId(userId);
            return ResponseEntity.ok(cartDto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // Remove a product from the user's cart
    @DeleteMapping("/remove/{userId}/{productId}")
    public ResponseEntity<CartDto> removeProductFromCart(@PathVariable Integer userId, @PathVariable Integer productId) {
        try {
            CartDto updatedCart = cartService.removeProductFromCart(userId, productId);
            return ResponseEntity.ok(updatedCart);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    //delete entire cart with products

    @DeleteMapping("/remove/{userId}")
    public void removeCartwithProducts(@PathVariable Integer userId){
        cartService.deletecartbyuserid(userId);
        ResponseEntity.ok("Cart deleted");
    }

}

