package com.mu54omd.mini_ecommerce.backend_maven.service;

import com.mu54omd.mini_ecommerce.backend_maven.entity.Order;
import com.mu54omd.mini_ecommerce.backend_maven.entity.OrderItem;
import com.mu54omd.mini_ecommerce.backend_maven.entity.Product;
import com.mu54omd.mini_ecommerce.backend_maven.entity.User;
import com.mu54omd.mini_ecommerce.backend_maven.repository.OrderRepository;
import com.mu54omd.mini_ecommerce.backend_maven.repository.ProductRepository;
import com.mu54omd.mini_ecommerce.backend_maven.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@Import({OrderService.class, ProductService.class})
@ActiveProfiles("test")
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    private User user;
    private Product product;

    @BeforeEach
    void setup() {
        user = new User();
        user.setUsername("sara");
        user.setEmail("sara@example.com");
        user.setPassword("12345");
        user = userRepository.save(user);

        product = new Product();
        product.setName("Keyboard");
        product.setDescription("Mechanical keyboard");
        product.setPrice(100.0);
        product.setStock(10);
        product = productService.addProduct(product);
    }

    @Test
    void testCreateOrderReducesStock() {
        OrderItem item = new OrderItem();
        item.setProduct(product);
        item.setQuantity(3);

        Order order = orderService.createOrder(user, List.of(item));

        Product updatedProduct = productRepository.findById(product.getId()).orElseThrow();
        assertThat(updatedProduct.getStock()).isEqualTo(7);

        List<Order> orders = orderService.getOrdersByUser(user);
        assertThat(orders).hasSize(1);
        assertThat(orders.get(0).getTotalPrice()).isEqualTo(300.0);
    }

    @Test
    void testCreateOrderInsufficientStockThrows() {
        OrderItem item = new OrderItem();
        item.setProduct(product);
        item.setQuantity(20);

        try {
            orderService.createOrder(user, List.of(item));
        } catch (RuntimeException ex) {
            assertThat(ex.getMessage()).contains("Insufficient stock");
        }
    }
}
