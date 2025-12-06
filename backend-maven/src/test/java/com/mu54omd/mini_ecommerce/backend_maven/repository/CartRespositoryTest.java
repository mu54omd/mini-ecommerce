package com.mu54omd.mini_ecommerce.backend_maven.repository;

import com.mu54omd.mini_ecommerce.backend_maven.entity.Cart;
import com.mu54omd.mini_ecommerce.backend_maven.entity.CartItem;
import com.mu54omd.mini_ecommerce.backend_maven.entity.Product;
import com.mu54omd.mini_ecommerce.backend_maven.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CartRepositoryTest {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void testSaveAndRetrieveCartWithItems() {
        User newUser = new User(null, "testuser", "user@test.com", "1234");
        User user = userRepository.save(newUser);
        Product product = productRepository.save(new Product(null, "Phone", "smartphone", "Smart", 999.0, 10));

        Cart cart = new Cart();
        cart.setUser(user);

        CartItem item = new CartItem();
        item.setCart(cart);
        item.setProduct(product);
        item.setQuantity(2);

        cart.setItems(List.of(item));
        Cart saved = cartRepository.save(cart);

        Cart found = cartRepository.findById(saved.getId()).orElseThrow();
        assertThat(found.getItems()).hasSize(1);
        assertThat(found.getItems().get(0).getProduct().getName()).isEqualTo("Phone");
    }
}

