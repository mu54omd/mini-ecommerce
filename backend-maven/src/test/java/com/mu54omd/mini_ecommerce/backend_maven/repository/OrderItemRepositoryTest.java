package com.mu54omd.mini_ecommerce.backend_maven.repository;

import com.mu54omd.mini_ecommerce.backend_maven.entity.Order;
import com.mu54omd.mini_ecommerce.backend_maven.entity.OrderItem;
import com.mu54omd.mini_ecommerce.backend_maven.entity.Product;
import com.mu54omd.mini_ecommerce.backend_maven.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class OrderItemRepositoryTest {
    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    private Order order;
    private Product product;

    @BeforeEach
    void setup() {
        User user = new User();
        user.setUsername("reza");
        user.setEmail("reza@example.com");
        user.setPassword("secret");
        userRepository.save(user);

        product = new Product();
        product.setName("Headphones");
        product.setDescription("Wireless headphones");
        product.setPrice(150.0);
        product.setStock(20);
        productRepository.save(product);

        order = new Order();
        order.setUser(user);
        order.setTotalPrice(150.0);
        orderRepository.save(order);
    }

    @Test
    void testSaveAndFindByOrder() {
        OrderItem item = new OrderItem();
        item.setOrder(order);
        item.setProduct(product);
        item.setQuantity(1);
        item.setPrice(product.getPrice());
        orderItemRepository.save(item);

        List<OrderItem> items = orderItemRepository.findByOrder(order);
        assertThat(items).hasSize(1);
        assertThat(items.get(0).getProduct().getName()).isEqualTo("Headphones");
    }
}
