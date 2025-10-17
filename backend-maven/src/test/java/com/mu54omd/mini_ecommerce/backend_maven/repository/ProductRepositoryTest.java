package com.mu54omd.mini_ecommerce.backend_maven.repository;

import com.mu54omd.mini_ecommerce.backend_maven.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void testSaveAndFindProduct() {
        // Arrange
        Product product = new Product();
        product.setName("Laptop");
        product.setDescription("A high-end laptop");
        product.setPrice(1800.0);
        product.setStock(10);

        // Act
        productRepository.save(product);

        // Assert
        List<Product> products = productRepository.findAll();
        assertThat(products).hasSize(1);
        assertThat(products.get(0).getName()).isEqualTo("Laptop");
    }

    @Test
    void testFindByNameContainingIgnoreCase() {
        Product p1 = new Product();
        p1.setName("iPhone");
        p1.setDescription("Apple phone");
        p1.setPrice(999.0);
        p1.setStock(5);

        productRepository.save(p1);

        List<Product> result = productRepository.findByNameContainingIgnoreCase("phone");
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("iPhone");
    }
}