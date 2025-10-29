package com.mu54omd.mini_ecommerce.backend_maven.service;

import com.mu54omd.mini_ecommerce.backend_maven.entity.Product;
import com.mu54omd.mini_ecommerce.backend_maven.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    public Product addProduct(Product product){
        return productRepository.save(product);
    }

    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    public List<Product> getAllProductsList(){
        return productRepository.findAll();
    }

    public void deleteProduct(Long productId){
        productRepository.deleteById(productId);
    }

    public List<Product> searchProducts(String keyword) {
        return productRepository.findByNameContainingIgnoreCase(keyword);
    }

    public Product updateStock(Long productId, int newStock){
        Product product = productRepository
                .findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        product.setStock(newStock);
        return productRepository.save(product);
    }
}
