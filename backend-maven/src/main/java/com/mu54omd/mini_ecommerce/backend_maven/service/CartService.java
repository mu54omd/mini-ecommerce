package com.mu54omd.mini_ecommerce.backend_maven.service;

import com.mu54omd.mini_ecommerce.backend_maven.entity.*;
import com.mu54omd.mini_ecommerce.backend_maven.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final OrderService orderService;

    public CartService(CartRepository cartRepository, ProductRepository productRepository, UserRepository userRepository, OrderService orderService) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.orderService = orderService;
    }

    public Cart getCartByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return cartRepository.findByUser(user)
                .orElseGet(() -> cartRepository.save(new Cart(user)));
    }

    public Cart addProductToCart(String username, Long productId, int quantity) {
        Cart cart = getCartByUsername(username);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        CartItem existingItem = cart.getItems().stream()
                .filter(i -> i.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
        } else {
            CartItem newItem = new CartItem(product, quantity);
            cart.addItem(newItem);
        }

        cart.recalculateTotal();
        return cartRepository.save(cart);
    }

    @Transactional
    public Cart removeProductFromCart(String username, Long productId) {
        Cart cart = getCartByUsername(username);
        cart.getItems().removeIf(item -> item.getProduct().getId().equals(productId));
        cart.recalculateTotal();
        return cartRepository.save(cart);
    }

    @Transactional
    public void clearCart(String username) {
        Cart cart = getCartByUsername(username);
        cart.getItems().clear();
        cartRepository.save(cart);
    }

    @Transactional
    public Order checkout(String username) {
        Cart cart = getCartByUsername(username);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (cart.getItems().isEmpty()) {
            throw new IllegalStateException("Cart is empty!");
        }

        List<OrderItem> orderItems = cart.getItems().stream()
                .map(item -> {
                    OrderItem oi = new OrderItem();
                    oi.setProduct(item.getProduct());
                    oi.setQuantity(item.getQuantity());
                    oi.setPrice(item.getProduct().getPrice());
                    return oi;
                })
                .collect(Collectors.toList());
        Order order = orderService.createOrder(user, orderItems);

        clearCart(username);
        return order;
    }
}
