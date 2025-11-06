package com.mu54omd.mini_ecommerce.backend_maven.controller;

import com.mu54omd.mini_ecommerce.backend_maven.dto.UploadImageResponse;
import com.mu54omd.mini_ecommerce.backend_maven.entity.Product;
import com.mu54omd.mini_ecommerce.backend_maven.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductRestController {

    private final ProductService productService;

    @Autowired
    public ProductRestController(ProductService productService){
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product){
        Product savedProduct = productService.addProduct(product);
        return ResponseEntity.ok(savedProduct);
    }

    @GetMapping
    public ResponseEntity<Page<Product>> getAllProducts(Pageable pageable) {
        return ResponseEntity.ok(productService.getAllProducts(pageable));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam String q) {
        return ResponseEntity.ok(productService.searchProducts(q));
    }

    @PutMapping("/{id}/stock")
    public ResponseEntity<Product> updateStock(@PathVariable Long id, @RequestParam int stock) {
        return ResponseEntity.ok(productService.updateStock(id, stock));
    }

    @DeleteMapping
    public ResponseEntity<?> deleteProduct(@RequestParam Long productId) throws IOException {
        Product product = productService.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (product.getImageUrl() != null) {
            String uploadDir = System.getProperty("user.home") + "/uploads/";
            String fileName = Paths.get(product.getImageUrl()).getFileName().toString();
            Path filePath = Paths.get(uploadDir + fileName);
            Files.deleteIfExists(filePath);
        }
        productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/upload-image")
    public ResponseEntity<?> uploadImage(@PathVariable Long id, @RequestParam("file") MultipartFile file) throws IOException {

        Product product = productService.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        String uploadDir = System.getProperty("user.home") + "/uploads/";
        Files.createDirectories(Paths.get(uploadDir));

        String fileName = id + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(uploadDir + fileName);
        Files.write(filePath, file.getBytes());

        String fileUrl = "/uploads/" + fileName;

        product.setImageUrl(fileUrl);
        productService.addProduct(product);
        return ResponseEntity.ok(new UploadImageResponse("Image Successfully Uploaded."));
    }
}
