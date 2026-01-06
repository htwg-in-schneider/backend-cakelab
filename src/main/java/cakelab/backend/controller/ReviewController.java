package cakelab.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import cakelab.backend.model.Review;
import cakelab.backend.dto.ReviewResponseDto;
import cakelab.backend.model.Cake;
import cakelab.backend.repository.ReviewRepository;
import cakelab.backend.repository.CakeRepository;
import cakelab.backend.dto.ReviewResponseDto;

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

    @PostMapping
    public ResponseEntity<Review> createReview(@RequestBody Review review) {
        Long cakeId = null;
        if (review != null && review.getCake() != null) {
            cakeId = review.getCake().getId();
        }
        LOG.info("Attempting to create review for cake id {}", cakeId);

        if (review == null) {
            LOG.warn("Review payload is null");
            return ResponseEntity.badRequest().build();
        }

        int stars = review.getStars();
        if (stars < 1 || stars > 5) {
            LOG.warn("Review stars out of bounds: {}", stars);
            return ResponseEntity.badRequest().build();
        }

        if (review.getCake() == null || review.getCake().getId() == null) {
            LOG.warn("Review cake is null or has no id");
            return ResponseEntity.badRequest().build();
        }

        Cake cake = cakeRepository.findById(review.getCake().getId()).orElse(null);
        if (cake == null) {
            LOG.warn("cake not found for review: {}", review.getCake().getId());
            return ResponseEntity.badRequest().build();
        }

        review.setCake(cake);
        Review saved = reviewRepository.save(review);
        LOG.info("Created review with id {}", saved.getId());
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteReview(@PathVariable Long id) {
        LOG.info("Attempting to delete review with id {}", id);
        Review review = reviewRepository.findById(id).orElse(null);
        if (review != null) {
            reviewRepository.delete(review);
            LOG.info("Deleted review with id {}", id);
            return ResponseEntity.noContent().build();
        } else {
            LOG.warn("Review not found for deletion: {}", id);
            return ResponseEntity.notFound().build();
        }
    }
}
