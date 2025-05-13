package com.specialistapp.config;


import com.specialistapp.model.entity.User;
import com.specialistapp.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class ModeratorInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ModeratorInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        String username = "moderator";
        if (userRepository.findByUsername(username).isEmpty()) {
            User moderator = new User();
            moderator.setUsername(username);
            moderator.setEmail("moderator@example.com");
            moderator.setPassword("{bcrypt}" + passwordEncoder.encode("moderator"));
            moderator.setRole("ROLE_MODERATOR");  // Добавьте эту строку
            userRepository.save(moderator);
            System.out.println("Модератор создан: логин = moderator, пароль = moderator");
        } else {
            System.out.println("Модератор уже существует");
        }
    }
}
