package com.mu54omd.mini_ecommerce.backend_maven.controller;

import com.mu54omd.mini_ecommerce.backend_maven.dto.AuthRequest;
import com.mu54omd.mini_ecommerce.backend_maven.dto.LoginResponse;
import com.mu54omd.mini_ecommerce.backend_maven.dto.RegisterRequest;
import com.mu54omd.mini_ecommerce.backend_maven.dto.RegisterResponse;
import com.mu54omd.mini_ecommerce.backend_maven.service.CustomUserDetailsService;
import com.mu54omd.mini_ecommerce.backend_maven.security.JwtUtil;
import com.mu54omd.mini_ecommerce.backend_maven.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthRestController {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    public AuthRestController(AuthenticationManager authenticationManager, CustomUserDetailsService userDetailsService, JwtUtil jwtUtil, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
            final String token = jwtUtil.generateToken(userDetails.getUsername(), userDetails.getAuthorities().toString());
            return ResponseEntity.ok(new LoginResponse(token));
        }catch (BadCredentialsException e){
            throw new RuntimeException("Invalid username or password");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest registerRequest){

        if(userService.findByUsername(registerRequest.getUsername()).isPresent()){
            throw new RuntimeException("Username already exists");
        }
        if(userService.findByEmail(registerRequest.getEmail()).isPresent()){
            throw new RuntimeException("Email already exists");
        }
        userService.registerUser(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new RegisterResponse("User registered successfully"));
    }
}
