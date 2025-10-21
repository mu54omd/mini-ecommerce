package com.mu54omd.mini_ecommerce.backend_maven.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mu54omd.mini_ecommerce.backend_maven.entity.Cart;
import com.mu54omd.mini_ecommerce.backend_maven.entity.CartItem;
import com.mu54omd.mini_ecommerce.backend_maven.entity.Product;
import com.mu54omd.mini_ecommerce.backend_maven.entity.User;
import com.mu54omd.mini_ecommerce.backend_maven.security.JwtUtil;
import com.mu54omd.mini_ecommerce.backend_maven.service.CartService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(CartRestController.class)
class CartRestControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private CartService cartService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @Test
    @WithMockUser(username = "john", roles = {"USER"})
    void testGetCartShouldReturnUserCart() throws Exception {
        User user = new User(1L, "john", "john@example.com", "pass");
        Product product = new Product(1L, "Phone", "Smart", 1000.0, 10);
        CartItem item = new CartItem(product, 2);
        Cart cart = new Cart(user);
        item.setCart(cart);
        cart.setItems(List.of(item));

        Mockito.when(cartService.getCartByUsername("john")).thenReturn(cart);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/cart"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].product.name").value("Phone"));
    }
}
