package com.mu54omd.mini_ecommerce.backend_maven.repository;

import com.mu54omd.mini_ecommerce.backend_maven.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    void testSaveAndFindByUsername() {
        User user = new User();
        user.setUsername("ali");
        user.setEmail("ali@example.com");
        user.setPassword("12345");

        userRepository.save(user);

        Optional<User> found = userRepository.findByUsername("ali");
        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("ali@example.com");
    }

    @Test
    void testExistsByEmail() {
        User user = new User();
        user.setUsername("mina");
        user.setEmail("mina@example.com");
        user.setPassword("pass");

        userRepository.save(user);

        boolean exists = userRepository.existsByEmail("mina@example.com");
        assertThat(exists).isTrue();
    }
}
