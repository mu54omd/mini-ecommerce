package com.mu54omd.mini_ecommerce.backend_maven.service;

import com.mu54omd.mini_ecommerce.backend_maven.dto.RegisterRequest;
import com.mu54omd.mini_ecommerce.backend_maven.entity.Cart;
import com.mu54omd.mini_ecommerce.backend_maven.entity.User;
import com.mu54omd.mini_ecommerce.backend_maven.repository.CartRepository;
import com.mu54omd.mini_ecommerce.backend_maven.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CartRepository cartRepository;
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, CartRepository cartRepository){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.cartRepository = cartRepository;
    }

    public void registerUser(RegisterRequest registerRequest){
        if(userRepository.existsByUsername(registerRequest.getUsername())){
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        User newUser = userRepository.save(user);
        cartRepository.save(new Cart(newUser));
    }

    public User createUser(User user){
        if(userRepository.existsByUsername(user.getUsername())){
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User newUser = userRepository.save(user);
        cartRepository.save(new Cart(user));
        return newUser;
    }

    public User editUser(Long userId, User newUser){
        User oldUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        User.Role role;
        try {
            role = User.Role.valueOf(newUser.getRole().toString().toUpperCase());
        }catch (IllegalArgumentException e){
            throw new RuntimeException("Invalid user role: " + newUser.getRole());
        }
        oldUser.setUsername(newUser.getUsername());
        oldUser.setEmail(newUser.getEmail());
        if(!newUser.getPassword().isBlank()) {
            oldUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        }
        oldUser.setRole(role);
        userRepository.save(oldUser);
        return oldUser;
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public void deleteUser(Long id){
        userRepository.deleteById(id);
    }
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

}
