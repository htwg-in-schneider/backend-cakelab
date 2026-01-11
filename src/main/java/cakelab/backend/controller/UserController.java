package cakelab.backend.controller;

import cakelab.backend.model.User;
import cakelab.backend.model.Cake;
import cakelab.backend.model.Role;
import cakelab.backend.repository.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")

public class UserController {
    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private boolean userFromJwtIsAdmin(Jwt jwt) {
        if (jwt == null || jwt.getSubject() == null) {
            LOG.warn("JWT or subject is null");
            return false;
        }
        Optional<User> user = userRepository.findByOauthId(jwt.getSubject());
        if (!user.isPresent() || user.get().getRole() != Role.ADMIN) {
            LOG.warn("Unauthorized access by " + user.map(u -> "user with oauthId " + u.getOauthId())
                    .orElse("unknown user"));
            return false;
        }
        return true;
    }

    @GetMapping
    public List<User> getAllUsers(@AuthenticationPrincipal Jwt jwt) {
        User currentUser = userRepository
                .findByOauthId(jwt.getSubject())
                .orElseThrow();

        if (currentUser.getRole() != Role.ADMIN) {
            throw new RuntimeException("Forbidden");
        }

        return userRepository.findAll();
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@AuthenticationPrincipal Jwt jwt,
            @PathVariable Long id, @RequestBody User userDetails) {
        if (!userFromJwtIsAdmin(jwt)) {
            return ResponseEntity.status(403).build();
        }
        Optional<User> opt = userRepository.findById(id);
        if (!opt.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        User user = opt.get();
        user.setId(id);
        user.setName(userDetails.getName());
        user.setOauthId(userDetails.getOauthId());
        user.setEmail(userDetails.getEmail());
        user.setRole(userDetails.getRole());
        User updateduser = userRepository.save(user);
        LOG.info("Updated user with id " + updateduser.getOauthId());
        return ResponseEntity.ok(updateduser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@AuthenticationPrincipal Jwt jwt, @PathVariable Long id) {
        if (!userFromJwtIsAdmin(jwt)) {
            return ResponseEntity.status(403).build();
        }

        Optional<User> opt = userRepository.findById(id);
        if (!opt.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        userRepository.delete(opt.get());
        LOG.info("Deleted user with id " + id);
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

}
