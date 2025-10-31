package com.mu54omd.mini_ecommerce.backend_maven.dto;

import com.mu54omd.mini_ecommerce.backend_maven.entity.Cart;
import com.mu54omd.mini_ecommerce.backend_maven.entity.CartItem;
import com.mu54omd.mini_ecommerce.backend_maven.entity.Product;

import java.util.stream.Collectors;

public class CartMapper {
    public static CartResponse toDto(Cart cart) {
        return new CartResponse(
                cart.getId(),
                cart.getUser().getUsername(),
                cart.getItems().stream()
                        .map(CartMapper::toItemDto)
                        .collect(Collectors.toList())
        );
    }

    private static CartItemResponse toItemDto(CartItem item) {
        return new CartItemResponse(
                item.getId(),
                toProductDto(item.getProduct()),
                item.getQuantity()
        );
    }

    private static ProductResponse toProductDto(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getImageUrl()
        );
    }
}
