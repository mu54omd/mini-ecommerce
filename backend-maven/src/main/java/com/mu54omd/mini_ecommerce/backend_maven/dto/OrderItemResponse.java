package com.mu54omd.mini_ecommerce.backend_maven.dto;

public record OrderItemResponse(
        Long id,
        Long productId,
        String productName,
        int quantity,
        double price
) {}
