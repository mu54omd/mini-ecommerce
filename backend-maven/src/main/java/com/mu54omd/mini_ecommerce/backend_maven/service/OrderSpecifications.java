package com.mu54omd.mini_ecommerce.backend_maven.service;

import com.mu54omd.mini_ecommerce.backend_maven.entity.Order;
import com.mu54omd.mini_ecommerce.backend_maven.entity.OrderItem;
import com.mu54omd.mini_ecommerce.backend_maven.entity.Product;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

public class OrderSpecifications {

    public static Specification<Order> hasStatus(Order.Status status) {
        return (root, query, cb) -> status == null ? null : cb.equal(root.get("status"), status);
    }

    public static Specification<Order> hasProductName(String productName) {
        return (root, query, cb) -> {
            if (productName == null || productName.isEmpty()) return null;
            query.distinct(true);
            Join<Order, OrderItem> itemsJoin = root.join("items", JoinType.LEFT);
            Join<OrderItem, Product> productJoin = itemsJoin.join("product", JoinType.LEFT);
            return cb.like(cb.lower(productJoin.get("name")), "%" + productName.toLowerCase() + "%");
        };
    }
}