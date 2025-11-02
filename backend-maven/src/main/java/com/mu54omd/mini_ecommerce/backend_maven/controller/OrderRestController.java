package com.mu54omd.mini_ecommerce.backend_maven.controller;

import com.mu54omd.mini_ecommerce.backend_maven.dto.OrderMapper;
import com.mu54omd.mini_ecommerce.backend_maven.dto.OrderResponse;
import com.mu54omd.mini_ecommerce.backend_maven.entity.Order;
import com.mu54omd.mini_ecommerce.backend_maven.entity.OrderItem;
import com.mu54omd.mini_ecommerce.backend_maven.entity.User;
import com.mu54omd.mini_ecommerce.backend_maven.service.OrderService;
import com.mu54omd.mini_ecommerce.backend_maven.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("api/orders")
public class OrderRestController {
    private final OrderService orderService;
    private final UserService userService;

    public OrderRestController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestParam String username,
                                             @RequestBody List<OrderItem> items) {
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Order order = orderService.createOrder(user, items);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<List<OrderResponse>> getOrdersByUser(@PathVariable String username) {
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        List<Order> orders = orderService.getOrdersByUser(user);
        return ResponseEntity.ok(OrderMapper.toDtoList(orders));
    }

    @GetMapping()
    public ResponseEntity<List<OrderResponse>> getOrders(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        List<Order> orders = orderService.getOrdersByUser(user);
        return ResponseEntity.ok(OrderMapper.toDtoList(orders));
    }



    @GetMapping("/status/{status}")
    public ResponseEntity<List<OrderResponse>> getOrdersByStatus(@PathVariable String status) {
        Order.Status s;
        try{
            s = Order.Status.valueOf(status.toUpperCase());
        }catch (IllegalArgumentException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid order status: " + status);
        }
        List<Order> orders = orderService.getOrdersByStatus(s);
        return ResponseEntity.ok(OrderMapper.toDtoList(orders));
    }

    @PostMapping("/status/{orderId}")
    public ResponseEntity<OrderResponse> updateOrderStatus(@PathVariable Long orderId, @RequestParam String status){
        Order.Status s;
        try{
            s = Order.Status.valueOf(status.toUpperCase());
        }catch (IllegalArgumentException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid order status: " + status);
        }
        return ResponseEntity.ok(OrderMapper.toDto(orderService.updateOrderStatus(orderId, s)));
    }
}
