package com.monks.user_service.controller;

import com.monks.user_service.model.User;
import com.monks.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {


    private final UserRepository userRepository;

    @GetMapping()
    public Flux<User> getAllUsers(@AuthenticationPrincipal Jwt jwt) {
        System.out.println("Jwt Claims : " + jwt.getClaims());
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    public Mono<User> getUserById(@AuthenticationPrincipal Jwt jwt, @PathVariable String id) {
        return userRepository.findById(id);
    }

    @GetMapping("/{name}")
    public Flux<User> getUserByName(@AuthenticationPrincipal Jwt jwt, @PathVariable String name) {
        return userRepository.findByName(name);
    }

    @PostMapping
    public Mono<User> createUser(@RequestBody User user) {
        return userRepository.save(user);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteUserById(@AuthenticationPrincipal Jwt jwt, @PathVariable String id) {
        return userRepository.deleteById(id);
    }
}