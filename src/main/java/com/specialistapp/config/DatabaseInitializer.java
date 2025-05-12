package com.specialistapp.config;


import com.specialistapp.model.entity.User;
import com.specialistapp.model.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DatabaseInitializer {

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Create initial moderator account if not exists
            if (userRepository.findByEmail("moderator@example.com").isEmpty()) {
                User moderator = new User();
                moderator.setUsername("moderator");
                moderator.setEmail("moderator@example.com");
                moderator.setPassword(passwordEncoder.encode("securepassword"));
                moderator.setRole("ROLE_MODERATOR");
                userRepository.save(moderator);
            }
        };
    }
}