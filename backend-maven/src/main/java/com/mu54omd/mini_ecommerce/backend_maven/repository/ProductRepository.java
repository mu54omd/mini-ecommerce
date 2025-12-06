package com.mu54omd.mini_ecommerce.backend_maven.repository;

import com.mu54omd.mini_ecommerce.backend_maven.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    Optional<Product> findByName(String name);
    @Query("""
    SELECT DISTINCT p.category
    FROM Product p
    WHERE p.isActive = true
      AND p.category IS NOT NULL
      AND p.category <> ''
""")
    List<String> findDistinctActiveCategories();
    Page<Product> findByCategoryAndIsActiveTrue(String category, Pageable pageable);
    Page<Product> findAllByIsActiveTrueOrderByCreatedAtDesc(Pageable pageable);
    Page<Product> findByIsActiveTrue(Pageable pageable);
    List<Product> findByNameContainingIgnoreCase(String name);
    List<Product> findByPriceBetween(Double min, Double max);
}
