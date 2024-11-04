package com.example.EcommerceBackend.Controller;


import com.example.EcommerceBackend.DTO.ReviewDto;
import com.example.EcommerceBackend.Service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping("/users/reviews/add")
    public ResponseEntity<ReviewDto> addReview(@RequestBody ReviewDto reviewDto) {
        ReviewDto savedReviewDto = reviewService.addReview(reviewDto);
        return ResponseEntity.ok(savedReviewDto);
    }

    @GetMapping("seller/product/{productId}")
    public ResponseEntity<List<ReviewDto>> getReviewsForProduct(@PathVariable int productId) {
        List<ReviewDto> reviews = reviewService.getReviewsForProduct(productId);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/users/{userId}")
    public List<ReviewDto> getAllReviewsByUserId(@PathVariable int userId) {
        return reviewService.getAllReviewsByUserId(userId);
    }
}

