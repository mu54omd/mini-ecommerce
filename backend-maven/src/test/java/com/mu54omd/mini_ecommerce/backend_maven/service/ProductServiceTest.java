package com.mu54omd.mini_ecommerce.backend_maven.service;

import com.mu54omd.mini_ecommerce.backend_maven.entity.Product;
import com.mu54omd.mini_ecommerce.backend_maven.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(ProductService.class)
@ActiveProfiles("test")
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void testAddAndRetrieveProduct() {
        Product product = new Product();
        product.setName("Laptop");
        product.setDescription("High-end laptop");
        product.setPrice(2000.0);
        product.setStock(5);

        Product saved = productService.addProduct(product);

        List<Product> products = productService.getAllProducts();
        assertThat(products).hasSize(1);
        assertThat(products.get(0).getName()).isEqualTo("Laptop");
    }

    @Test
    void testUpdateStock() {
        Product product = new Product();
        product.setName("Mouse");
        product.setDescription("Wireless mouse");
        product.setPrice(50.0);
        product.setStock(10);

        Product saved = productService.addProduct(product);
        productService.updateStock(saved.getId(), 7);

        Product updated = productRepository.findById(saved.getId()).orElseThrow();
        assertThat(updated.getStock()).isEqualTo(7);
    }
}
