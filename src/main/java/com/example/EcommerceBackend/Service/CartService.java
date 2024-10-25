package com.example.EcommerceBackend.Service;

import com.example.EcommerceBackend.DTO.CartDto;
import com.example.EcommerceBackend.DTO.ProductDto;
import com.example.EcommerceBackend.Entity.Cart;
import com.example.EcommerceBackend.Entity.Product;
import com.example.EcommerceBackend.Entity.User;
import com.example.EcommerceBackend.Entity.WishList;
import com.example.EcommerceBackend.Repository.CartRepository;
import com.example.EcommerceBackend.Repository.ProductRepository;
import com.example.EcommerceBackend.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepo userRepo;

    // Add products to the user's cart
    public CartDto addProductsToUserCart(CartDto cartDto, int userId) throws Exception {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new Exception("User with ID " + userId + " not found"));

        Cart cart = user.getCart();
        if (cart == null) {
            cart = new Cart();
            cart.setUser(user);
        }

        List<Product> products = productRepository.findAllById(cartDto.getProduct().stream()
                .map(ProductDto::getId)
                .collect(Collectors.toList()));

        cart.getProducts().addAll(products);
        cartRepository.save(cart);

        return convertCartToDto(cart);
    }


    public CartDto getCartByUserId(Integer userId) throws Exception {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new Exception("User with ID " + userId + " not found"));

        Cart cart = user.getCart();
        if (cart == null) {
            throw new Exception("Cart not found for user with ID " + userId);
        }
        CartDto cartDto=new CartDto();
        cartDto.setTotalPrice(cart.calculateTotalPrice());

        return convertCartToDto(cart);
    }
    // Method to remove a product from the cart
    public CartDto removeProductFromCart(Integer userId, Integer productId) throws Exception {
        // Find the cart by user ID
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new Exception("Cart not found for user ID: " + userId));

        // Find the product by product ID
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new Exception("Product not found with ID: " + productId));

        // Remove the product from the cart
        boolean removed = cart.getProducts().removeIf(p -> p.getId().equals(productId));

        if (!removed) {
            throw new Exception("Product not present in the cart");
        }

        // Save the updated cart
        cartRepository.save(cart);

        // Return the updated cart as a DTO
        return convertCartToDto(cart);
    }

    public void deletecartbyuserid(int userId) throws RuntimeException {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        cart.getProducts().clear(); // Clear products in the cart
        cartRepository.delete(cart); // Delete the cart
    }

    private CartDto convertCartToDto(Cart cart) {
        CartDto cartDto = new CartDto();
        cartDto.setUserid(cart.getUser().getId());
        cartDto.setTotalPrice(cart.calculateTotalPrice()); // Set total price here

        List<ProductDto> productDtos = cart.getProducts().stream()
                .map(product -> new ProductDto(product.getId(), product.getName(),
                        product.getPrice(), product.getDescription()))
                .collect(Collectors.toList());
        cartDto.setProduct(productDtos);
        return cartDto;
    }



}
