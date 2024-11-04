package com.example.EcommerceBackend.Controller;

import com.example.EcommerceBackend.DTO.WishlistDto;
import com.example.EcommerceBackend.Service.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wishlist")
public class WishlistController {

    @Autowired
    private WishlistService wishlistService;

    @GetMapping("/users/{userId}")
    public WishlistDto getWishlistByUserId(@PathVariable int userId) {
        return wishlistService.getWishlistByUserId(userId);
    }

    @PostMapping("/users/{userId}/add/{productId}")
    public void addProductToWishlist(@PathVariable int userId, @PathVariable int productId) {
        wishlistService.addProductToWishlist(userId, productId);
    }

    @DeleteMapping("/users/{userId}/remove/{productId}")
    public void removeProductFromWishlist(@PathVariable int userId, @PathVariable int productId) {
        wishlistService.removeProductFromWishlist(userId, productId);
    }
}

