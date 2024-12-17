package com.example.EcommerceBackend.Controller;

import com.example.EcommerceBackend.DTO.AuthRequest;
import com.example.EcommerceBackend.Service.UserImplementationService;
import com.example.EcommerceBackend.Util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    private final UserImplementationService userService;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, UserDetailsService userDetailsService, JwtUtil jwtUtil, UserImplementationService userService) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }
    @PostMapping("/generate-otp")
    public String generateOTP(@RequestParam String email){
        userService.genearateOtpandSend(email);
        return "OTP sent";
    }
    @PostMapping("/verify-otp")
    public String verifyOTP(@RequestParam String email,String otp){
       boolean isverified= userService.verifyOtp(email,otp);
        if(isverified){
            return "verified";
        }
        return "not verified";
    }

    @PostMapping("/authenticate")
    public String generateToken(@RequestBody AuthRequest authRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );
        } catch (Exception ex) {
            throw new Exception("Invalid username/password");
        }
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
        return jwtUtil.generateToken(userDetails);  // Pass the entire userDetails object here
    }
}
