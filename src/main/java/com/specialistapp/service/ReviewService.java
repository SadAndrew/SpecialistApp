package com.specialistapp.service;

import com.specialistapp.model.entity.Review;
import com.specialistapp.model.entity.Specialist;
import com.specialistapp.model.entity.User;
import com.specialistapp.model.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    public Review createReview(User user, Specialist specialist, int rating, String comment) {
        Review review = new Review();
        review.setUser(user);
        review.setSpecialist(specialist);
        review.setRating(rating);
        review.setComment(comment);
        return reviewRepository.save(review);
    }

    public double calculateAverageRating(Specialist specialist) {
        List<Review> reviews = reviewRepository.findBySpecialist(specialist);
        return reviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);
    }

    public List<Review> findBySpecialist(Specialist specialist) {
        return reviewRepository.findBySpecialist(specialist);
    }
    public void save(Review review) {
        reviewRepository.save(review);
    }
}