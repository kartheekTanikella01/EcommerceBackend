package com.example.EcommerceBackend.Repository;

import com.example.EcommerceBackend.Entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart,Integer> {
    Optional<Cart> findByUser_Id(int userId);

    Optional<Cart> findByUserId(int userId);
}
