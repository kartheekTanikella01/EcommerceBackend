package com.example.EcommerceBackend.Filter;

import com.example.EcommerceBackend.Entity.User;
import com.example.EcommerceBackend.Repository.UserRepo;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class OtpVerificationFilter extends OncePerRequestFilter {

    private final UserRepo userRepo;
    @Autowired
    public OtpVerificationFilter(UserRepo userRepo) {
        this.userRepo = userRepo;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            User user = userRepo.findByEmail(authentication.getName());
            if (user != null && !user.isOtpVerified()) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "OTP verification required");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
 }

