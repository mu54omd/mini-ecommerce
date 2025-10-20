package com.mu54omd.mini_ecommerce.backend_maven.repository;

import com.mu54omd.mini_ecommerce.backend_maven.entity.Order;
import com.mu54omd.mini_ecommerce.backend_maven.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    void setup() {
        User user = new User();
        user.setUsername("javad");
        user.setEmail("javad@example.com");
        user.setPassword("pass");
        testUser = userRepository.save(user);
    }

    @Test
    void testSaveAndFindByUser() {
        Order order = new Order();
        order.setUser(testUser);
        order.setTotalPrice(500.0);
        orderRepository.save(order);

        List<Order> found = orderRepository.findByUser(testUser);
        assertThat(found).hasSize(1);
        assertThat(found.get(0).getTotalPrice()).isEqualTo(500.0);
    }

    @Test
    void testFindByStatus() {
        Order order = new Order();
        order.setUser(testUser);
        order.setTotalPrice(100.0);
        order.setStatus(Order.Status.PAID);
        orderRepository.save(order);

        List<Order> result = orderRepository.findByStatus(Order.Status.PAID);
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getStatus()).isEqualTo(Order.Status.PAID);
    }
}
