package cakelab.backend.controller;

import cakelab.backend.model.Order;
import cakelab.backend.model.Cake;
import cakelab.backend.model.User;
import cakelab.backend.repository.CakeRepository;
import cakelab.backend.repository.OrderRepository;
import cakelab.backend.repository.UserRepository;
import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderRepository orderRepo;
    @Autowired
    private CakeRepository cakeRepo;
     @Autowired
    private UserRepository userRepo;
    private static final Logger LOG = LoggerFactory.getLogger(ReviewController.class);

    @PostMapping
    public Order createOrder(@Valid @RequestBody Order order,  @AuthenticationPrincipal Jwt jwt) {
        if (order.getItems() == null || order.getItems().isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Order must contain at least one item");
        }

        order.getItems().forEach(item -> {
            item.setCake(cakeRepo.findById(item.getCake().getId())
            
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.BAD_REQUEST, "Cake not found")));
            item.setOrder(order);
        });
         String oauthId = jwt.getSubject(); 
    User user = userRepo.findByOauthId(oauthId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found"));
    
    order.setUser(user);
        return orderRepo.save(order);
    }

    @GetMapping
    public List<Order> getOrders() {
        return orderRepo.findAll();
    }

    @GetMapping("/{id}")
    public Order getOrderById(@PathVariable Long id) {
        return orderRepo.findById(id).orElse(null);
    }

    @PutMapping("order/{id}")
    public Order updateOrder(@PathVariable Long id, @RequestBody Order updatedOrder) {

        return orderRepo.findById(id).map(order -> {
            order.setStatus(updatedOrder.getStatus());
            return orderRepo.save(order);
        }).orElse(null);
    }

    @PatchMapping("/{id}/finish")
    public Order finishOrder(@PathVariable Long id) {
        return orderRepo.findById(id).map(order -> {
            order.setStatus("fertig");
            return orderRepo.save(order);
        }).orElse(null);
    }

    @PatchMapping("/{id}/cancel")
    public Order cancelOrder(@PathVariable Long id) {
        return orderRepo.findById(id).map(order -> {
            order.setStatus("storniert");
            return orderRepo.save(order);
        }).orElse(null);
    }
  
    @PatchMapping("/{id}")
    public Order updateStatus(@PathVariable Long id, @RequestBody java.util.Map<String, String> body) {
        String newStatus = body.get("status");
        return orderRepo.findById(id).map(order -> {
            order.setStatus(newStatus);
            return orderRepo.save(order);
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Bestellung nicht gefunden"));
    }
}
