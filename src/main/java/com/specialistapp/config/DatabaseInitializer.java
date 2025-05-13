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
            if (userRepository.findByEmail("moderator@example.com").isEmpty()) {
                User moderator = new User();
                moderator.setUsername("moderator");
                moderator.setEmail("moderator@example.com");
                moderator.setPassword(passwordEncoder.encode("moderator")); // Единый пароль
                moderator.setRole("ROLE_MODERATOR");
                userRepository.save(moderator);
                System.out.println("Модератор создан: логин = moderator, пароль = moderator");
            } else {
                System.out.println("Модератор уже существует");
            }
        };
    }
}