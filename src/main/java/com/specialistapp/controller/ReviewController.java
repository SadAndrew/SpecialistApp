package com.specialistapp.controller;

import com.specialistapp.model.entity.Review;
import com.specialistapp.model.entity.Specialist;
import com.specialistapp.model.entity.User;
import com.specialistapp.service.ReviewService;
import com.specialistapp.service.SpecialistService;
import com.specialistapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private SpecialistService specialistService;

    @Autowired
    private UserService userService;

    @GetMapping("/{specialistId}")
    public String showSpecialistReviews(@PathVariable Long specialistId, Model model) {
        Specialist specialist = specialistService.findById(specialistId);
        List<Review> reviews = reviewService.findBySpecialist(specialist);

        model.addAttribute("specialist", specialist);
        model.addAttribute("reviews", reviews);
        model.addAttribute("newReview", new Review());

        return "reviews/review-list";
    }

    @PostMapping("/{specialistId}/add")
    public String addReview(@PathVariable Long specialistId,
                            @ModelAttribute Review newReview,
                            @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByEmail(userDetails.getUsername());
        Specialist specialist = specialistService.findById(specialistId);

        newReview.setUser(user);
        newReview.setSpecialist(specialist);

        reviewService.save(newReview);

        return "redirect:/reviews/" + specialistId;
    }
}
