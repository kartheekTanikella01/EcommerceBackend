package com.example.EcommerceBackend.Service;

import com.example.EcommerceBackend.DTO.ProductDto;
import com.example.EcommerceBackend.DTO.WishlistDto;
import com.example.EcommerceBackend.Entity.Product;
import com.example.EcommerceBackend.Entity.User;
import com.example.EcommerceBackend.Entity.Wishlist;
import com.example.EcommerceBackend.Repository.ProductRepository;
import com.example.EcommerceBackend.Repository.UserRepo;
import com.example.EcommerceBackend.Repository.WishlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WishlistService {

    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepo userRepository;

    public WishlistDto getWishlistByUserId(int userId) {
        Wishlist wishlist = wishlistRepository.findByUser_Id(userId)
                .orElseThrow(() -> new RuntimeException("Wishlist not found for this user"));

        return convertToWishlistDto(wishlist);
    }

    public WishlistDto convertToWishlistDto(Wishlist wishlist) {
        WishlistDto wishlistDto = new WishlistDto();
        wishlistDto.setId(wishlist.getId());
        wishlistDto.setUserId(wishlist.getUser().getId());

        List<ProductDto> productDtos = wishlist.getProducts().stream().map(product -> {
            ProductDto productDto = new ProductDto();
            productDto.setId(product.getId());
            productDto.setName(product.getName());
            productDto.setPrice(product.getPrice());
            return productDto;
        }).collect(Collectors.toList());

        wishlistDto.setProducts(productDtos);
        return wishlistDto;
    }

    public void addProductToWishlist(int userId, int productId) {
        Wishlist wishlist = wishlistRepository.findByUser_Id(userId).orElse(null);

        if (wishlist == null) {
            wishlist = new Wishlist();

            // Fetch user by userId and set it in the wishlist
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            wishlist.setUser(user);

            wishlist.setProducts(new ArrayList<>());
        }

        // Fetch the product using productId
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        wishlist.getProducts().add(product);
        wishlistRepository.save(wishlist);
    }

    public void removeProductFromWishlist(int userId, int productId) {
        Wishlist wishlist = wishlistRepository.findByUser_Id(userId)
                .orElseThrow(() -> new RuntimeException("Wishlist not found for this user"));

        wishlist.getProducts().removeIf(product -> product.getId() == productId);
        wishlistRepository.save(wishlist);
    }
}
