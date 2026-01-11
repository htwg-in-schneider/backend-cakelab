package cakelab.backend.controller;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import cakelab.backend.model.Order;
import cakelab.backend.model.User;
import cakelab.backend.repository.OrderRepository;
import cakelab.backend.repository.UserRepository;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    static final Logger LOGGER = LoggerFactory.getLogger(ProfileController.class);

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderRepository orderRepo;

    @GetMapping
    public ResponseEntity<User> getProfile(@AuthenticationPrincipal Jwt jwt) {
        String oauthId = jwt.getSubject();
        LOGGER.info("getProfile called for principal: {}", oauthId);
        LOGGER.debug("JWT claims: {}", jwt.getClaims());

        if (oauthId == null) {
            LOGGER.warn("JWT does not contain 'sub' claim");
            return ResponseEntity.badRequest().build();
        }
        return userRepository.findByOauthId(oauthId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@AuthenticationPrincipal Jwt jwt,
            @PathVariable Long id, @RequestBody User userDetails) {

        Optional<User> opt = userRepository.findById(id);
        if (!opt.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        User user = opt.get();
        user.setEmail(userDetails.getEmail());
        user.setName(userDetails.getName());
        User updateduser = userRepository.save(user);
        LOGGER.info("Updated user with id " + updateduser.getOauthId());
        return ResponseEntity.ok(updateduser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@AuthenticationPrincipal Jwt jwt, @PathVariable Long id) {

        Optional<User> opt = userRepository.findById(id);
        if (!opt.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        userRepository.delete(opt.get());
        LOGGER.info("Deleted user with id " + id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> opt = userRepository.findById(id);
        if (opt.isPresent()) {
            return ResponseEntity.ok(opt.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

   @GetMapping("/orders")
    public ResponseEntity<List<Order>> getOrdersForProfile(@AuthenticationPrincipal Jwt jwt) {
      String oauthId = jwt.getSubject();
    LOGGER.info("Fetching orders for profile {}", oauthId);

    if (oauthId == null) {
        LOGGER.warn("JWT does not contain 'sub' claim");
        return ResponseEntity.badRequest().build();
    }

    return userRepository.findByOauthId(oauthId)
            .map(user -> {
                List<Order> orders = orderRepo.findByUserId(user.getId());
                LOGGER.info("Found {} orders for user {}", orders.size(), user.getId());
                return ResponseEntity.ok(orders);
            })
            .orElse(ResponseEntity.notFound().build());
}


}