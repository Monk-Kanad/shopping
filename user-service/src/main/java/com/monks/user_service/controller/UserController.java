package com.monks.user_service.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final List<User> USERS = List.of(
            new User(1L, "Alice", "alice@example.com"),
            new User(2L, "Bob", "bob@example.com"),
            new User(3L, "Charlie", "charlie@example.com")
    );

    @GetMapping()
    public Flux<User> getAllUsers(@AuthenticationPrincipal Jwt jwt) {
        System.out.println("Jwt Claims : " + jwt.getClaims());
        return Flux.fromIterable(USERS);
    }

    @GetMapping("/{id}")
    public Mono<User> getUserById(@AuthenticationPrincipal Jwt jwt, @PathVariable Long id) {
        return Mono.justOrEmpty(USERS.stream()
                .filter(user -> user.id().equals(id))
                .findFirst());
    }
}

record User(Long id, String name, String email) {
}
