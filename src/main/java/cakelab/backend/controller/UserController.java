package cakelab.backend.controller;

import cakelab.backend.model.User;
import cakelab.backend.model.Role;
import cakelab.backend.repository.UserRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")

public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
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
}
