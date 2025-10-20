package com.mu54omd.mini_ecommerce.backend_maven.security;

import com.mu54omd.mini_ecommerce.backend_maven.entity.User;
import com.mu54omd.mini_ecommerce.backend_maven.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository, PasswordEncoder encoder) {
        return args -> {
            if (userRepository.findByUsername("testuser").isEmpty()) {
                User user = new User();
                user.setUsername("testuser");
                user.setEmail("testuser@test.com");
                user.setPassword(encoder.encode("123456"));
                user.setRole(User.Role.USER);
                userRepository.save(user);
                System.out.println("✅ Default user created: testuser / 123456");
            }
        };
    }
}
