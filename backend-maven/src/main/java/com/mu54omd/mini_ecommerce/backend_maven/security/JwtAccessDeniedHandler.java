package com.mu54omd.mini_ecommerce.backend_maven.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        String json = String.format("{\"status\":403,\"error\":\"Forbidden Action\",\"message\":\"%s\",\"timestamp\":\"%s\"}",
                accessDeniedException.getMessage(),
                LocalDateTime.now());
        response.getWriter().write(json);
    }
}