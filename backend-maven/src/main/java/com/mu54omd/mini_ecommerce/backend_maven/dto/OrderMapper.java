package com.mu54omd.mini_ecommerce.backend_maven.dto;

import com.mu54omd.mini_ecommerce.backend_maven.entity.Order;

import java.util.List;
import java.util.stream.Collectors;

public class OrderMapper {

    public static OrderResponse toDto(Order order) {
        List<OrderItemResponse> items = order.getItems().stream()
                .map(oi -> new OrderItemResponse(
                        oi.getId(),
                        oi.getProduct().getId(),
                        oi.getProduct().getName(),
                        oi.getQuantity(),
                        oi.getPrice()
                ))
                .collect(Collectors.toList());

        return new OrderResponse(
                order.getId(),
                order.getUser().getUsername(),
                order.getCreatedAt(),
                order.getStatus().name(),
                order.getTotalPrice(),
                items
        );
    }

    public static List<OrderResponse> toDtoList(List<Order> orders) {
        return orders.stream().map(OrderMapper::toDto).collect(Collectors.toList());
    }
}