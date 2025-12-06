package com.mu54omd.mini_ecommerce.backend_maven.service;

import com.mu54omd.mini_ecommerce.backend_maven.entity.Product;
import com.mu54omd.mini_ecommerce.backend_maven.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.mu54omd.mini_ecommerce.backend_maven.service.ProductSpecifications.*;

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

    public List<String> getCategories(){
        return productRepository.findDistinctActiveCategories();
    }

    public Page<Product> getActiveProductsByCategory(String category, Pageable pageable) {
        return productRepository.findByCategoryAndIsActiveTrue(category, pageable);
    }

    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    public Page<Product> getAllActiveProducts(Pageable pageable){
        return productRepository.findByIsActiveTrue(pageable);
    }

    public Page<Product> getAllLatestActiveProducts(Pageable pageable){
        return productRepository.findAllByIsActiveTrueOrderByCreatedAtDesc(pageable);
    }

    public void deactivateProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        product.setActive(false);
        productRepository.save(product);
    }

    public List<Product> getAllProductsList(){
        return productRepository.findAll();
    }

    public void deleteProduct(Long productId){
        productRepository.deleteById(productId);
    }

    public Product editProduct(Product product){
        Product theProduct = productRepository
                .findById(product.getId())
                .orElseThrow(() -> new RuntimeException("Product not found"));
        theProduct.setName(product.getName());
        theProduct.setDescription(product.getDescription());
        theProduct.setPrice(product.getPrice());
        theProduct.setStock(product.getStock());
        return productRepository.save(theProduct);
    }

    public List<Product> searchProducts(String keyword) {
        return productRepository.findByNameContainingIgnoreCase(keyword);
    }

    public Page<Product> filterProducts(String q, String category, Pageable pageable) {
        Specification<Product> spec = ProductSpecifications.isActive();

        if (q != null && !q.isBlank()) {
            spec = spec.and(ProductSpecifications.nameContains(q));
        }

        if (category != null && !category.isBlank()) {
            spec = spec.and(ProductSpecifications.categoryEquals(category));
        }

        return productRepository.findAll(spec, pageable);
    }

    public Product updateStock(Long productId, int newStock){
        Product product = productRepository
                .findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        product.setStock(newStock);
        return productRepository.save(product);
    }

    public Optional<Product> findById(Long productId){
        return productRepository.findById(productId);
    }

    public Optional<Product> findByName(String name){
        return productRepository.findByName(name);
    }
}
