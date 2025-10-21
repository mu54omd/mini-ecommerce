package com.mu54omd.mini_ecommerce.backend_maven.dto;

public record ProductResponse(
        Long id,
        String name,
        String description,
        Double price
) {
}
