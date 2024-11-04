package com.example.EcommerceBackend.Controller;

import com.example.EcommerceBackend.DTO.AddressDto;
import com.example.EcommerceBackend.Entity.User;
import com.example.EcommerceBackend.Service.UserImplementationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

public class UserController {

    private final UserImplementationService userService;

    @Autowired
    public UserController(UserImplementationService userService) {
        this.userService = userService;
    }

    // Public endpoint for user registration
    @PostMapping("/register")
    public User registerUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    // Admin endpoints
    @GetMapping("/admin")
    public List<User> getAllUsers() {
        return userService.findAllUsers();
    }

    @GetMapping("/admin/{id}")
    public ResponseEntity<User> getUserById(@PathVariable int id) {
        return userService.findUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/admin/{id}")
    public ResponseEntity<User> updateUser(@PathVariable int id, @RequestBody User user) {
        return ResponseEntity.ok(userService.updateUser(id, user));
    }

    @DeleteMapping("/admin/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable int id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    // User endpoints for address management
    @PostMapping("/{userId}/address")
    public ResponseEntity<String> addAddressToUser(@PathVariable int userId, @RequestBody AddressDto addressDTO) {
        userService.addAddressToUser(userId, addressDTO);
        return ResponseEntity.ok("Address added successfully");
    }

    @GetMapping("/admin/{userId}/addresses")
    public List<AddressDto> getUserAddresses(@PathVariable int userId) {
        return userService.getUserAddresses(userId);
    }
}
