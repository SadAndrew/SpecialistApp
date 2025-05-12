package com.specialistapp.model.repository;

import com.specialistapp.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);

    @Query("SELECT u FROM User u WHERE u.role = :role")
    List<User> findByRole(@Param("role") String role);

    @Query("SELECT u FROM User u WHERE u.role IN ('ROLE_USER', 'ROLE_SPECIALIST')")
    List<User> findAllRegularUsers();
}