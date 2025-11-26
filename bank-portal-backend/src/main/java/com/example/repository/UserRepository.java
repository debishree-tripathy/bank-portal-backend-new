package com.example.repository;

import com.example.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    // New method to find by username OR email
    Optional<User> findByUsernameOrEmail(String username, String email);

    // Methods to check duplicates
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);

    // ✅ Check if a user with a role exists
    boolean existsByRole(String role);
}
