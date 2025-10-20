package com.mu54omd.mini_ecommerce.backend_maven.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mu54omd.mini_ecommerce.backend_maven.dto.AuthRequest;
import com.mu54omd.mini_ecommerce.backend_maven.entity.User;
import com.mu54omd.mini_ecommerce.backend_maven.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    private String validToken;

    @BeforeEach
    void setUp() throws Exception {
        AuthRequest request = new AuthRequest();
        request.setUsername("testuser");
        request.setPassword("123456");


        User user = new User(1L, "sara", "sara@example.com", "1234", null);
        when(userService.findByUsername("sara")).thenReturn(Optional.of(user));

        String response = mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andReturn()
                .getResponse()
                .getContentAsString();

        validToken = objectMapper.readTree(response).get("token").asText();
    }

    @Test
    void testLoginShouldReturnJwtTokenWhenCredentialsAreValid() throws Exception {
        AuthRequest request = new AuthRequest();
        request.setUsername("testuser");
        request.setPassword("123456");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    void testLoginShouldFailWhenPasswordIsInvalid() throws Exception {
        AuthRequest request = new AuthRequest();
        request.setUsername("testuser");
        request.setPassword("wrongpassword");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testAccessProtectedEndpointShouldFailWhenNoTokenProvided() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/by-username?username=sara"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testAccessProtectedEndpointShouldFailWhenInvalidTokenProvided() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/users")
                        .header("Authorization", "Bearer invalid.token.value"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testAccessProtectedEndpointShouldSucceedWhenValidTokenProvided() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/by-username?username=sara")
                        .header("Authorization", "Bearer " + validToken))
                .andExpect(status().isOk());
    }
}


