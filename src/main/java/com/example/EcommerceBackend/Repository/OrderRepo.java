package com.example.EcommerceBackend.Repository;


import com.example.EcommerceBackend.Entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepo extends JpaRepository<Order,Integer> {
    void deleteByUserId(Integer userId);
}
