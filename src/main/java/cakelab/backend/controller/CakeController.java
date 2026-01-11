package cakelab.backend.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import cakelab.backend.model.Category;
import cakelab.backend.model.Cake;
import cakelab.backend.model.User;
import cakelab.backend.model.Role;
import cakelab.backend.repository.CakeRepository;
import cakelab.backend.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/cake")
public class CakeController {

    private static final Logger LOG = LoggerFactory.getLogger(CakeController.class);
 @Autowired
    private UserRepository userRepository;

    @Autowired
    private CakeRepository cakeRepository;
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
    public List<Cake> getCakes(@RequestParam(required = false) String name, 
        @RequestParam(required = false) Category category) {
        if (name != null && category != null) {
            return cakeRepository.findByNameContainingIgnoreCaseAndCategory(name, category);
        } else if (name != null) {
            return cakeRepository.findByNameContainingIgnoreCase(name);
        } else if (category != null) {
            return cakeRepository.findByCategory(category);
        } else {
            return cakeRepository.findAll();
        }
    }
    
    @PostMapping
   public ResponseEntity<Cake> createCake(@AuthenticationPrincipal Jwt jwt, @RequestBody Cake cake) {
        if (!userFromJwtIsAdmin(jwt)) {
            return ResponseEntity.status(403).build();
        }

        if (cake.getId() != null) {
            cake.setId(null);
            LOG.warn("Attempted to create a cake with an existing ID. ID has been set to null to create a new cake.");
        }
        Cake newcake = cakeRepository.save(cake);
        LOG.info("Created new cake with id " + newcake.getId());
        return ResponseEntity.ok(newcake);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cake> updateCake(@AuthenticationPrincipal Jwt jwt, 
        @PathVariable Long id, @RequestBody Cake cakeDetails) {
        if (!userFromJwtIsAdmin(jwt)) {
            return ResponseEntity.status(403).build();
        }
        Optional<Cake> opt = cakeRepository.findById(id);
        if (!opt.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Cake cake = opt.get();
       cake.setCategory(cakeDetails.getCategory());
        cake.setBeschreibung(cakeDetails.getBeschreibung());
        cake.setBildUrl(cakeDetails.getBildUrl());
        cake.setPreis(cakeDetails.getPreis());
        cake.setName(cakeDetails.getName());
        Cake updatedcake = cakeRepository.save(cake);
        LOG.info("Updated cake with id " + updatedcake.getId());
        return ResponseEntity.ok(updatedcake);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteCake(@AuthenticationPrincipal Jwt jwt,@PathVariable Long id) {
        if (!userFromJwtIsAdmin(jwt)) {
            return ResponseEntity.status(403).build();
        }

        Optional<Cake> opt = cakeRepository.findById(id);
        if (!opt.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        cakeRepository.delete(opt.get());
        LOG.info("Deleted cake with id " + id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cake> getCakeById(@PathVariable Long id) {
        Optional<Cake> opt = cakeRepository.findById(id);
        if (opt.isPresent()) {
            return ResponseEntity.ok(opt.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}