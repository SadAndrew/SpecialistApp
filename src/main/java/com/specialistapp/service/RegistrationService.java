package com.specialistapp.service;

import com.specialistapp.model.entity.Specialist;
import com.specialistapp.model.entity.User;
import com.specialistapp.model.repository.SpecialistRepository;
import com.specialistapp.model.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SpecialistRepository specialistRepository;

    public User registerUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already in use");
        }
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already in use");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("ROLE_USER");
        return userRepository.save(user);
    }

    @Transactional
    public Specialist registerSpecialist(Specialist specialist) {
        if (userRepository.findByEmail(specialist.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already in use");
        }
        if (userRepository.findByUsername(specialist.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already in use");
        }

        specialist.setPassword(passwordEncoder.encode(specialist.getPassword()));
        specialist.setRole("ROLE_SPECIALIST");


        return specialistRepository.save(specialist);
    }
}
