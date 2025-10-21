package com.mu54omd.mini_ecommerce.backend_maven.controller;

import com.mu54omd.mini_ecommerce.backend_maven.dto.CartMapper;
import com.mu54omd.mini_ecommerce.backend_maven.dto.CartResponse;
import com.mu54omd.mini_ecommerce.backend_maven.entity.Cart;
import com.mu54omd.mini_ecommerce.backend_maven.entity.Order;
import com.mu54omd.mini_ecommerce.backend_maven.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartRestController {

    private final CartService cartService;

    public CartRestController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public ResponseEntity<CartResponse> getCart(@AuthenticationPrincipal UserDetails user) {
        Cart cart = cartService.getCartByUsername(user.getUsername());
        return ResponseEntity.ok(CartMapper.toDto(cart));
    }

    @PostMapping("/add")
    public ResponseEntity<CartResponse> addProduct(@AuthenticationPrincipal UserDetails user,
                                           @RequestParam Long productId,
                                           @RequestParam int quantity) {
        Cart updatedCart = cartService.addProductToCart(user.getUsername(), productId, quantity);
        return ResponseEntity.ok(CartMapper.toDto(updatedCart));
    }

    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<CartResponse> removeProduct(@AuthenticationPrincipal UserDetails user,
                                              @PathVariable Long productId) {
        Cart updatedCart = cartService.removeProductFromCart(user.getUsername(), productId);
        return ResponseEntity.ok(CartMapper.toDto(updatedCart));
    }

    @DeleteMapping("/clear")
    public ResponseEntity<String> clearCart(@AuthenticationPrincipal UserDetails user) {
        cartService.clearCart(user.getUsername());
        return ResponseEntity.ok("Cart cleared successfully!");
    }

    @PostMapping("/checkout")
    public ResponseEntity<String> checkout(@AuthenticationPrincipal UserDetails user) {
        Order order = cartService.checkout(user.getUsername());
        return ResponseEntity.ok("Checkout successful! Order ID: " + order.getId());
    }
}
