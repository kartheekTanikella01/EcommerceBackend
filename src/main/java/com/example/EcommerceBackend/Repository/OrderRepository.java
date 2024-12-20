package com.example.EcommerceBackend.Repository;

import com.example.EcommerceBackend.Entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Integer> {
    List<Order> findByUserId(int userId);
}
