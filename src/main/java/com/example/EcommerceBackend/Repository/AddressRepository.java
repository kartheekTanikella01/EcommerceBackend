package com.example.EcommerceBackend.Repository;

import com.example.EcommerceBackend.Entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address,Integer> {
}
