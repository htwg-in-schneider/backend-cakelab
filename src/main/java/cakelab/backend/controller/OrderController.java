package cakelab.backend.controller;

import cakelab.backend.model.Order;
import cakelab.backend.repository.OrderRepository;
import jakarta.validation.Valid;

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
@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    @Autowired
    private OrderRepository orderRepo;

    @PostMapping
    public Order createOrder(@Valid @RequestBody Order order) {
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

    @PutMapping("/{id}")
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
 }

