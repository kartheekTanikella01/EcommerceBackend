package com.example.EcommerceBackend.Repository;

import com.example.EcommerceBackend.Entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review,Integer> {
    List<Review> findByProductId(int productId);
    List<Review> findByUserId(int userId);
}
