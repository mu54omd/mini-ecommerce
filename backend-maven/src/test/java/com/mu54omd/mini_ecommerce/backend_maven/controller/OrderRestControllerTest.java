package com.mu54omd.mini_ecommerce.backend_maven.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mu54omd.mini_ecommerce.backend_maven.entity.Order;
import com.mu54omd.mini_ecommerce.backend_maven.entity.OrderItem;
import com.mu54omd.mini_ecommerce.backend_maven.entity.Product;
import com.mu54omd.mini_ecommerce.backend_maven.entity.User;
import com.mu54omd.mini_ecommerce.backend_maven.security.JwtFilter;
import com.mu54omd.mini_ecommerce.backend_maven.security.JwtUtil;
import com.mu54omd.mini_ecommerce.backend_maven.service.OrderService;
import com.mu54omd.mini_ecommerce.backend_maven.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(OrderRestController.class)
class OrderRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrderService orderService;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetOrdersByUserShouldReturnList() throws Exception {
        User user = new User(1L, "sara", "sara@example.com", "1234");
        Product product = new Product(1L, "Laptop","laptop", "Computer", 2000.0, 3);
        OrderItem orderItem = new OrderItem();
        Order order = new Order();
        order.setId(1L);
        orderItem.setOrder(order);
        orderItem.setProduct(product);
        orderItem.setQuantity(2);
        orderItem.setPrice(product.getPrice());
        order.setItems(List.of(orderItem));
        order.setUser(user);
        order.setTotalPrice(orderItem.getPrice() * orderItem.getQuantity());

        when(userService.findByUsername("sara")).thenReturn(Optional.of(user));


        when(orderService.getOrdersByUser(user)).thenReturn(List.of(order));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/orders/user/sara"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }
}
