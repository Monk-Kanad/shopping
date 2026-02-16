package com.monks.order_service.controller;

import com.monks.order_service.model.Order;
import com.monks.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderRepository orderRepository;

    @GetMapping
    public Flux<Order> findAll(@AuthenticationPrincipal Jwt jwt) {
        return orderRepository.findAll();
    }

    @GetMapping("/{id}")
    public Mono<Order> findById(@AuthenticationPrincipal Jwt jwt, @PathVariable String id) {
        return orderRepository.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Order> create(@AuthenticationPrincipal Jwt jwt, @RequestBody Order order) {
        // You can add business logic here (calculate total, set status, etc.)
        if (order.getStatus() == null) {
            order.setStatus("PENDING");
        }
        if (order.getCreatedAt() == null) {
            order.setCreatedAt(Instant.now());
        }
        order.setUpdatedAt(Instant.now());

        // Simple total calculation example (you can make it more robust)
        if (order.getItems() != null) {
            double total = order.getItems().stream()
                    .mapToDouble(item -> item.getQuantity() * item.getUnitPrice())
                    .sum();
            order.setTotalAmount(total);
        }

        return orderRepository.save(order);
    }

    @PutMapping("/{id}")
    public Mono<Order> update(@AuthenticationPrincipal Jwt jwt, @PathVariable String id, @RequestBody Order order) {
        return orderRepository.findById(id)
                .flatMap(existing -> {
                    // Update allowed fields (protect ID, createdAt, etc.)
                    existing.setCustomerId(order.getCustomerId());
                    existing.setCustomerName(order.getCustomerName());
                    existing.setEmail(order.getEmail());
                    existing.setItems(order.getItems());
                    existing.setStatus(order.getStatus());
                    existing.setUpdatedAt(Instant.now());

                    // Recalculate total if items changed
                    if (order.getItems() != null) {
                        double total = order.getItems().stream()
                                .mapToDouble(item -> item.getQuantity() * item.getUnitPrice())
                                .sum();
                        existing.setTotalAmount(total);
                    }

                    return orderRepository.save(existing);
                });
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@AuthenticationPrincipal Jwt jwt, @PathVariable String id) {
        return orderRepository.deleteById(id);
    }

    // Custom queries
    @GetMapping("/customer/{customerId}")
    public Flux<Order> findByCustomer(@AuthenticationPrincipal Jwt jwt, @PathVariable String customerId) {
        return orderRepository.findByCustomerId(customerId);
    }

    @GetMapping("/status/{status}")
    public Flux<Order> findByStatus(@AuthenticationPrincipal Jwt jwt, @PathVariable String status) {
        return orderRepository.findByStatus(status);
    }
}
