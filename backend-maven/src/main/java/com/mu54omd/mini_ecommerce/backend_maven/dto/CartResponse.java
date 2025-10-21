package com.mu54omd.mini_ecommerce.backend_maven.dto;

import java.util.List;

public record CartResponse(
        Long id,
        String username,
        List<CartItemResponse> items
) {}
