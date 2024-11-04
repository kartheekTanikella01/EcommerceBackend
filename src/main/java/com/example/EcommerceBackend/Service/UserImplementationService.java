package com.example.EcommerceBackend.Service;

import com.example.EcommerceBackend.DTO.AddressDto;
import com.example.EcommerceBackend.Entity.Address;
import com.example.EcommerceBackend.Entity.User;
import com.example.EcommerceBackend.Entity.UserPrinciple;
import com.example.EcommerceBackend.Repository.AddressRepository;
import com.example.EcommerceBackend.Repository.UserRepo;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserImplementationService implements UserDetailsService {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final AddressRepository addressRepository;



    @Autowired
    public UserImplementationService(UserRepo userRepo, PasswordEncoder passwordEncoder, AddressRepository addressRepository) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.addressRepository = addressRepository;
    }

    public List<User> findAllUsers() {
        return userRepo.findAll();
    }

    public Optional<User> findUserById(int id) {
        return userRepo.findById(id);
    }

    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    public User updateUser(int id, User updatedUser) {
        return userRepo.findById(id).map(user -> {
            user.setName(updatedUser.getName());
            user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
            user.setRoles(updatedUser.getRoles());
            return userRepo.save(user);
        }).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void deleteUser(int id) {
        userRepo.deleteById(id);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user=userRepo.findByName(username);

        if (username==null){
            System.out.println("user not found");
            throw  new UsernameNotFoundException("User not found ");
        }

        return new UserPrinciple(user);
    }

    public void addAddressToUser(int userId, AddressDto addressDTO) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Address address = new Address();
        address.setStreet(addressDTO.getStreet());
        address.setCity(addressDTO.getCity());
        address.setState(addressDTO.getState());
        address.setCountry(addressDTO.getCountry());
        address.setZipCode(addressDTO.getZipCode());
        address.setUser(user);

        user.getAddresses().add(address);
        userRepo.save(user);
    }

    public List<AddressDto> getUserAddresses(int userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<AddressDto> addressDTOs = new ArrayList<>();
        for (Address address : user.getAddresses()) {
            AddressDto dto = new AddressDto();
            dto.setStreet(address.getStreet());
            dto.setCity(address.getCity());
            dto.setState(address.getState());
            dto.setCountry(address.getCountry());
            dto.setZipCode(address.getZipCode());
            addressDTOs.add(dto);
        }
        return addressDTOs;
    }



}
