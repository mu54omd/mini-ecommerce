package com.mu54omd.mini_ecommerce.backend_maven.repository;

import com.mu54omd.mini_ecommerce.backend_maven.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByName(String name);
    List<Product> findByNameContainingIgnoreCase(String name);
    List<Product> findByPriceBetween(Double min, Double max);
}
