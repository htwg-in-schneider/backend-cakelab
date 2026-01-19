package cakelab.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import cakelab.backend.model.Review;
import cakelab.backend.dto.ReviewResponseDto;
import cakelab.backend.model.Cake;
import cakelab.backend.repository.ReviewRepository;
import cakelab.backend.repository.CakeRepository;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/review")
public class ReviewController {

    private static final Logger LOG = LoggerFactory.getLogger(ReviewController.class);

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private CakeRepository cakeRepository;

    @GetMapping
    public List<Review> getAllReviews() {
        LOG.info("Fetching all reviews");
        List<Review> reviews = reviewRepository.findAll();
        LOG.info("Found {} reviews", reviews != null ? reviews.size() : 0);
        return reviews;
    }

@GetMapping("/cake/{cakeId}")
public List<ReviewResponseDto> getReviewsByCake(@PathVariable Long cakeId) {
    return reviewRepository.findByCakeId(cakeId)
        .stream()
        .map(r -> new ReviewResponseDto(
            r.getId(),
            r.getStars(),
            r.getText(),
            r.getUser() != null ? r.getUser().getName() : "Anonym"
        ))
        .toList();
}
}