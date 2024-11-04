package com.example.EcommerceBackend.Repository;

import com.example.EcommerceBackend.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User,Integer > {

   User findByName(String name);

}
