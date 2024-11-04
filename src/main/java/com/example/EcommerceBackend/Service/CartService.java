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
import java.util.Optional;
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
    public void addProductToCart(int userId, int productId, int quantity) {
        // Fetch user by ID
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Fetch product by ID
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Get or create the user's cart
        Cart cart;
        Optional<Cart> existingCart = cartRepository.findByUserId(userId); // Add method in CartRepository
        if (existingCart.isPresent()) {
            cart = existingCart.get();
        } else {
            cart = new Cart();
            cart.setUser(user); // Set the user here
            cart.setTotalPrice(0.0); // Initialize total price
        }

        // Create cart item
        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setQuantity(quantity);
        cartItem.setCart(cart);
        cart.getItems().add(cartItem); // Add the item to the cart
        cart.setTotalPrice(cart.getTotalPrice() + (product.getPrice() * quantity)); // Update total price

        // Save the cart
        cartRepository.save(cart);
    }


    public CartDto getCart(int userId) {
        Cart cart = cartRepository.findByUser_Id(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found for this user"));
        return mapToCartDto(cart);
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
