package com.example.EcommerceBackend.Service;

import com.example.EcommerceBackend.DTO.CartDto;
import com.example.EcommerceBackend.DTO.CartItemDto;
import com.example.EcommerceBackend.Entity.*;
import com.example.EcommerceBackend.Repository.CartRepository;
import com.example.EcommerceBackend.Repository.ProductRepository;

import com.example.EcommerceBackend.Repository.UserRepo;
import com.example.EcommerceBackend.Repository.WishlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private UserRepo userRepository;

    @Autowired
    private ProductRepository productRepository;
    public Map<String, Object> addProductsToCart(int userId, List<CartItemDto> items) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = cartRepository.findByUserId(userId).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUser(user);
            newCart.setTotalPrice(0.0);
            return newCart;
        });

        List<CartItemDto> cartItemDtos = new ArrayList<>();
        double totalCost = cart.getTotalPrice();

        for (CartItemDto item : items) {
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            CartItem cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setQuantity(item.getQuantity());
            cartItem.setCart(cart);

            double subtotal = product.getPrice() * item.getQuantity();
            totalCost += subtotal;

            cart.getItems().add(cartItem);

            // Populate the CartItemDto for each item in the response
            CartItemDto cartItemDto = new CartItemDto(
                    product.getId(),
                    product.getName(),
                    item.getQuantity(),
                    product.getPrice(),
                    subtotal
            );
            cartItemDtos.add(cartItemDto);
        }

        cart.setTotalPrice(totalCost);
        cartRepository.save(cart);

        // Return both items and total cost in a map
        Map<String, Object> response = new HashMap<>();
        response.put("items", cartItemDtos);
        response.put("totalCost", totalCost);

        return response;
    }


    public Map<String, Object> getCartResponse(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        List<CartItemDto> cartItemDtos = new ArrayList<>();
        double totalCost = 0.0;

        for (CartItem cartItem : cart.getItems()) {
            Product product = cartItem.getProduct();
            int quantity = cartItem.getQuantity();
            double subtotal = product.getPrice() * quantity;
            totalCost += subtotal;

            CartItemDto cartItemDto = new CartItemDto(
                    product.getId(),
                    product.getName(),
                    quantity,
                    product.getPrice(),
                    subtotal
            );
            cartItemDtos.add(cartItemDto);
        }

        // Create response map
        Map<String, Object> response = new HashMap<>();
        response.put("items", cartItemDtos);
        response.put("totalCost", totalCost);

        return response;
    }


    public void clearCart(int userId) {
        Cart cart = cartRepository.findByUser_Id(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found for this user"));

        Wishlist wishlist = wishlistRepository.findByUser_Id(userId)
                .orElseGet(() -> {
                    Wishlist newWishlist = new Wishlist();
                    newWishlist.setUser(cart.getUser());
                    return wishlistRepository.save(newWishlist);
                });

        cart.getItems().forEach(cartItem -> wishlist.getProducts().add(cartItem.getProduct()));

        cart.getItems().clear();
        cart.setTotalPrice(0.0);

        wishlistRepository.save(wishlist);
        cartRepository.save(cart);
    }

    private CartDto mapToCartDto(Cart cart) {
        CartDto cartDto = new CartDto();
        cartDto.setId(cart.getId());
        cartDto.setTotalPrice(cart.getTotalPrice());
        cartDto.setItems(cart.getItems().stream().map(this::mapToCartItemDto).collect(Collectors.toList()));
        return cartDto;
    }

    private CartItemDto mapToCartItemDto(CartItem item) {
        CartItemDto dto = new CartItemDto();
        dto.setProductId(item.getProduct().getId());
        dto.setProductName(item.getProduct().getName());
        dto.setQuantity(item.getQuantity());
        dto.setPrice(item.getProduct().getPrice());
        dto.setSubtotal(item.getSubtotal());
        return dto;
    }
}
