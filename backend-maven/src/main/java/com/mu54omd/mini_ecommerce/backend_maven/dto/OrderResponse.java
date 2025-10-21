package com.mu54omd.mini_ecommerce.backend_maven.dto;

import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
        Long id,
        String username,
        LocalDateTime createdAt,
        String status,
        double totalPrice,
        List<OrderItemResponse> items
) {}
