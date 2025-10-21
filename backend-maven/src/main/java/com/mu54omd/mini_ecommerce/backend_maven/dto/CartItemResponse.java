package com.mu54omd.mini_ecommerce.backend_maven.dto;

public record CartItemResponse(
        Long id,
        ProductResponse product,
        int quantity
) {
}
