package com.example.EcommerceBackend.Service;

import com.example.EcommerceBackend.DTO.ReviewDTO;
import com.example.EcommerceBackend.Entity.Product;
import com.example.EcommerceBackend.Entity.Reviews;
import com.example.EcommerceBackend.Entity.User;
import com.example.EcommerceBackend.Repository.ProductRepository;
import com.example.EcommerceBackend.Repository.ReviewRepository;
import com.example.EcommerceBackend.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private UserRepo userRepo;
    private ProductRepository productRepository;
//Add a review
    public ReviewDTO addReview(ReviewDTO reviewDTO){
        User user =userRepo.findById(reviewDTO.getUserId())
                .orElseThrow(
                        ()->new RuntimeException("USer not found")
                );
        Product product=productRepository.findById(reviewDTO.getProductId())
                .orElseThrow(
                        ()-> new RuntimeException("Product not found")
                );
        Reviews reviews=new Reviews();
        reviews.setId(reviewDTO.getId());
        reviews.setProduct(product);
        reviews.setStars(reviewDTO.getStars());
        reviews.setComments(reviewDTO.getComment());
        reviews.setUser(user);

        reviewRepository.save(reviews);
        return converReviewToDTO(reviews);

    }

    public List<ReviewDTO> fetchReviewsByUserId(int userId) {
        // Check if the user exists
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Retrieve all reviews for the user
        Optional<Reviews> reviewsList = reviewRepository.findByUserId(userId);

        // Check if any reviews are found
        if (reviewsList.isEmpty()) {
            throw new RuntimeException("No reviews found for user ID " + userId);
        }

        // Convert the list of Reviews to a list of ReviewDTOs
        return reviewsList.stream()
                .map(this::converReviewToDTO)
                .collect(Collectors.toList());
    }

    public List<ReviewDTO> getReviewsByProductId(Integer productId) {
        List<Reviews> reviews = reviewRepository.findByProductId(productId);
        return reviews.stream().map(this::converReviewToDTO).collect(Collectors.toList());
    }

    public ReviewDTO converReviewToDTO(Reviews reviews){
        ReviewDTO reviewDTO=new ReviewDTO();
        return new ReviewDTO(reviewDTO.getId(),
                reviewDTO.getUserId(),
                reviewDTO.getProductId(),
                reviewDTO.getStars(),
                reviewDTO.getComment());

    }


}
