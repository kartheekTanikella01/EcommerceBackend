package com.example.EcommerceBackend.Service;
import com.example.EcommerceBackend.DTO.UserDTO;


import com.example.EcommerceBackend.Entity.Address;
import com.example.EcommerceBackend.Entity.User;
import com.example.EcommerceBackend.Repository.AddressRepository;
import com.example.EcommerceBackend.Repository.OrderRepo;
import com.example.EcommerceBackend.Repository.UserRepo;
import com.example.EcommerceBackend.enums.UserRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserDTO createUser(UserDTO userdto) {

        User user = new User();
        user.setFirstName(userdto.getFirstName());
        user.setLastName(userdto.getLastName());
        user.setEmail(userdto.getEmail());

        // Encode the user's password before saving
        String encodedPassword = passwordEncoder.encode(userdto.getPassword());
        user.setPassword(encodedPassword); // Set the encoded password

        // Convert roles from DTO to entity
        List<UserRoles> roles = userdto.getUserRoles().stream()
                .map(UserRoles::valueOf)
                .collect(Collectors.toList());
        user.setUserRoles(roles);

        User savedUser = userRepo.save(user);
        return convertToUserDto(savedUser);


    }

    public UserDTO getUserById(int userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User with ID not found"));
        return convertToUserDto(user);
    }

    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    public UserDTO updateUser(Integer id, UserDTO userDto) throws Exception {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new Exception("User with ID " + id + " not found"));

        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());

        // Updating the roles if needed
        List<UserRoles> roles = userDto.getUserRoles().stream()
                .map(UserRoles::valueOf)
                .collect(Collectors.toList());
        user.setUserRoles(roles);

        User updatedUser = userRepo.save(user);
        return convertToUserDto(updatedUser);
    }

    @Transactional
    public void deleteUser(Integer userId) throws Exception {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new Exception("User with ID " + userId + " not found"));

        if (user.getCart() != null) user.setCart(null);
        if (user.getWishList() != null) user.setWishList(null);

        orderRepo.deleteByUserId(userId);

        List<Address> addresses = user.getAddresses();
        if (addresses != null && !addresses.isEmpty()) {
            for (Address address : addresses) {
                addressRepository.delete(address);
            }
        }

        userRepo.delete(user);
    }

    private UserDTO convertToUserDto(User user) {
        List<String> roles = user.getUserRoles().stream()
                .map(Enum::name) // Convert enum values to strings
                .collect(Collectors.toList());

        return new UserDTO(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(),user.getPassword(), roles);
    }


}

