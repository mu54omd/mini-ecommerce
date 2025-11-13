package com.mu54omd.mini_ecommerce.backend_maven.security;

import com.mu54omd.mini_ecommerce.backend_maven.entity.Product;
import com.mu54omd.mini_ecommerce.backend_maven.entity.User;
import com.mu54omd.mini_ecommerce.backend_maven.repository.ProductRepository;
import com.mu54omd.mini_ecommerce.backend_maven.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository, PasswordEncoder encoder, ProductRepository productRepository) {
        return args -> {
            if (userRepository.findByUsername("user").isEmpty()) {
                User user = new User();
                user.setUsername("user");
                user.setEmail("user@user.com");
                user.setPassword(encoder.encode("user"));
                user.setRole(User.Role.USER);
                userRepository.save(user);
                System.out.println("✅ Default user created: user / user");
            }
            if (userRepository.findByUsername("admin").isEmpty()) {
                User user = new User();
                user.setUsername("admin");
                user.setEmail("admin@admin.com");
                user.setPassword(encoder.encode("admin"));
                user.setRole(User.Role.ADMIN);
                userRepository.save(user);
                System.out.println("✅ Default user created: admin / admin");
            }
        };
    }
}
