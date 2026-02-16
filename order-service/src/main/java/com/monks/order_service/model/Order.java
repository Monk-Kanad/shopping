package com.monks.order_service.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "orders")
public class Order {

    @Id
    private String id;

    private String customerId;
    private String customerName;
    private String email;
    private List<OrderItem> items;
    private Double totalAmount;
    private String status;// e.g. "PENDING", "CONFIRMED", "SHIPPED", "DELIVERED", "CANCELLED"
    private Instant createdAt = Instant.now();
    private Instant updatedAt = Instant.now();

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItem {
        private String productId;
        private String productName;
        private Integer quantity;
        private Double unitPrice;
        private Double subtotal;
    }
}