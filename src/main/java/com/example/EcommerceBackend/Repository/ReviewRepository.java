package com.example.EcommerceBackend.Repository;

import com.example.EcommerceBackend.Entity.Reviews;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Reviews,Integer> {

    Optional<Reviews> findByUserId(Integer userId);

    List<Reviews> findByProductId(Integer productId);
}
