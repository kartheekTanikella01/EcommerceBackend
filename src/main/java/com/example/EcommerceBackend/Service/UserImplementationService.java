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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class UserImplementationService implements UserDetailsService {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final AddressRepository addressRepository;

    private final EmailService emailService;



    @Autowired
    public UserImplementationService(UserRepo userRepo, PasswordEncoder passwordEncoder, AddressRepository addressRepository, EmailService emailService) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.addressRepository = addressRepository;
        this.emailService = emailService;
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
            user.setEmail(updatedUser.getEmail());
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

        User user=userRepo.findByEmail(username);

        if (user==null){
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

    //generate the otp
    public void genearateOtpandSend(String email){
        User user=userRepo.findByEmail(email);
        if(user== null){
            throw  new RuntimeException("USer not found");
        }

        //Generate otp(6 letter)
        String otp=String.valueOf(100000 + new Random().nextInt(900000));
        user.setOtp(otp);
        user.setLocalDateTime(LocalDateTime.now().plusMinutes(2));
        userRepo.save(user);
        // Send OTP email
        String subject = "Your OTP for Login";
        String body = "Dear User,\n\nYour OTP for login is: " + otp + "\n\nThis OTP will expire in 2 minutes.\n\nThank you!";
        emailService.sendOtpEmail(email, subject, body);

        System.out.println("OTP sent to " + email);

        System.out.println("otp sent to"+email);
    }

    //method to verify the otp
    public boolean verifyOtp(String email,String otp){
        User user=userRepo.findByEmail(email);
        if(user== null || user.getOtp()==null){
            throw new RuntimeException("invalid username or otp");
        }
        if (user.getOtp().equals(otp) && user.getLocalDateTime().isAfter(LocalDateTime.now())) {
            user.setOtp(null);
            user.setLocalDateTime(null);
            user.setOtpVerified(true);
            userRepo.save(user);
            return true;
        } else {
            throw new RuntimeException("Invalid or Expired OTP");
        }

    }



}
