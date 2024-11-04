package com.example.EcommerceBackend.Repository;

import com.example.EcommerceBackend.Entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WishlistRepository extends JpaRepository<Wishlist,Integer> {
    Optional<Wishlist> findByUser_Id(int userId);
}
