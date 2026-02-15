package com.monks.order_service.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @GetMapping
    public Flux<Order> getOrders(@AuthenticationPrincipal Jwt jwt) {
        System.out.println("Jwt Claims : " + jwt.getClaims());
        return Flux.fromIterable(List.of(new Order(1, "Pizza"), new Order(2, "Burger")));
    }

}

record Order(Integer orderId, String itemName) {
}
