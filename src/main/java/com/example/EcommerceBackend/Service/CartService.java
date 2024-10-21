package com.example.EcommerceBackend.Service;

import com.example.EcommerceBackend.DTO.CartDto;
import com.example.EcommerceBackend.DTO.ProductDto;
import com.example.EcommerceBackend.Entity.Cart;
import com.example.EcommerceBackend.Entity.Product;
import com.example.EcommerceBackend.Entity.User;
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
    public CartDto addProductsToUserCart(CartDto cartDto,int userId) throws Exception {
        // Fetch the user by ID
        User user = userRepo.findById(cartDto.getUserid())
                .orElseThrow(() -> new Exception("User with ID " + cartDto.getUserid() + " not found"));

        // Get or create the user's cart
        Cart cart = user.getCart();

        if (cart == null) {
            cart = new Cart();
            cart.setUser(user);
        }

        // Fetch products by their IDs from the DTO
        List<Product> products = productRepository.findAllById(cartDto.getProduct().stream()
                .map(ProductDto::getId)
                .collect(Collectors.toList()));

        // Add products to the cart
        cart.getProducts().addAll(products);

        // Save the cart
        cartRepository.save(cart);

        // Return updated CartDto
        return convertCartToDto(cart);
    }

    public CartDto getCartByUserId(Integer userId) throws Exception {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new Exception("User with ID " + userId + " not found"));

        Cart cart = user.getCart();
        if (cart == null) {
            throw new Exception("Cart not found for user with ID " + userId);
        }

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

//fetch all products from a specific category

    private CartDto convertCartToDto(Cart cart) {
        CartDto cartDto = new CartDto();
        cartDto.setUserid(cart.getUser().getId());
        List<ProductDto> productDtos = cart.getProducts().stream()
                .map(product -> new ProductDto(product.getId(), product.getName(),
                        product.getPrice(),product.getDescription()
                ))
                .collect(Collectors.toList());
        cartDto.setProduct(productDtos);
        return cartDto;
    }


}
