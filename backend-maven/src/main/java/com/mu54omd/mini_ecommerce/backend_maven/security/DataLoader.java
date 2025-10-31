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
            if (productRepository.findByName("Iphone 14").isEmpty()) {
                Product product = new Product();
                product.setName("Iphone 14");
                product.setDescription("Apple iPhone 14");
                product.setStock(50);
                product.setPrice(675.0);
                product.setImageUrl("http://192.168.1.101:5050/uploads/iphone_14.jpg");
                productRepository.save(product);
                System.out.println("Iphone 14 added.");
            }

            if (productRepository.findByName("Iphone 15").isEmpty()) {
                Product product = new Product();
                product.setName("Iphone 15");
                product.setDescription("Apple iPhone 15");
                product.setStock(50);
                product.setPrice(700.0);
                product.setImageUrl("http://192.168.1.101:5050/uploads/iphone_15.jpg");
                productRepository.save(product);
                System.out.println("Iphone 15 added.");
            }

            if (productRepository.findByName("Iphone 17").isEmpty()) {
                Product product = new Product();
                product.setName("Iphone 17");
                product.setDescription("Apple iPhone 17");
                product.setStock(50);
                product.setPrice(1000.0);
                product.setImageUrl("http://192.168.1.101:5050/uploads/iphone_17.jpg");
                productRepository.save(product);
                System.out.println("Iphone 17 added.");
            }

            if (productRepository.findByName("Macbook Air 15").isEmpty()) {
                Product product = new Product();
                product.setName("Macbook Air 15");
                product.setDescription("Apple Macbook Air 15 inch");
                product.setStock(50);
                product.setPrice(3500.0);
                product.setImageUrl("http://192.168.1.101:5050/uploads/macbook_air_15.jpg");
                productRepository.save(product);
                System.out.println("Macbook Air 15 added.");
            }
            if(productRepository.findByName("Macbook Air M2").isEmpty()) {
                Product product = new Product();
                product.setName("Macbook Air M2");
                product.setDescription("Apple Macbook Air M2");
                product.setStock(50);
                product.setPrice(3500.0);
                product.setImageUrl("http://192.168.1.101:5050/uploads/macbook_air_m2.jpg");
                productRepository.save(product);
                System.out.println("Macbook Air M2 added.");
            }
            if(productRepository.findByName("Dell G15 5530").isEmpty()) {
                Product product = new Product();
                product.setName("Dell G15 5530");
                product.setDescription("Dell Gaming Laptop");
                product.setStock(50);
                product.setPrice(3500.0);
                product.setImageUrl("http://192.168.1.101:5050/uploads/dell-g15-5530.jpg");
                productRepository.save(product);
                System.out.println("Dell G15 5530 added.");
            }
            if(productRepository.findByName("Dell Alienware 16X").isEmpty()) {
                Product product = new Product();
                product.setName("Dell Alienware 16X");
                product.setDescription("Dell Alienware 16X Aurora Gaming Laptop");
                product.setStock(50);
                product.setPrice(3500.0);
                product.setImageUrl("http://192.168.1.101:5050/uploads/dell-g15-5530.jpg");
                productRepository.save(product);
                System.out.println("Dell Alienware 16X Aurora added.");
            }

        };
    }
}
