package com.mu54omd.mini_ecommerce.backend_maven.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mu54omd.mini_ecommerce.backend_maven.entity.Product;
import com.mu54omd.mini_ecommerce.backend_maven.security.JwtFilter;
import com.mu54omd.mini_ecommerce.backend_maven.security.JwtUtil;
import com.mu54omd.mini_ecommerce.backend_maven.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(ProductRestController.class)
class ProductRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private JwtUtil jwtUtil;

    @Test
    void testGetAllProductsShouldReturnList() throws Exception {
        when(productService.getAllProducts())
                .thenReturn(List.of(new Product(1L, "Laptop", "Good", 2500.0, 5)));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Laptop"));
    }

    @Test
    void testCreateProductShouldReturnCreatedProduct() throws Exception {
        Product product = new Product(null, "Phone", "Smart", 1200.0, 10);
        when(productService.addProduct(any(Product.class))).thenReturn(product);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Phone"));
    }
}
