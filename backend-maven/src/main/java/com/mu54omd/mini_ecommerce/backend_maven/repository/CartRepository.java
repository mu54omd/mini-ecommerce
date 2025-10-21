package com.mu54omd.mini_ecommerce.backend_maven.repository;

import com.mu54omd.mini_ecommerce.backend_maven.entity.Cart;
import com.mu54omd.mini_ecommerce.backend_maven.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByUser(User user);
}
