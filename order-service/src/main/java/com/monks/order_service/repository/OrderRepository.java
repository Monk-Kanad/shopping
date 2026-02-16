package com.monks.order_service.repository;


import com.monks.order_service.model.Order;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

import java.time.Instant;

public interface OrderRepository extends ReactiveMongoRepository<Order, String> {

    Flux<Order> findByCustomerId(String customerId);

    Flux<Order> findByStatus(String status);

    Flux<Order> findByTotalAmountBetween(Double min, Double max);

    Flux<Order> findByCreatedAtAfter(Instant date);
}