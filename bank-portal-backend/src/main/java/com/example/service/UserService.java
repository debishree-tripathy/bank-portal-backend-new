package com.example.service;

import com.example.model.User;
import com.example.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Save user as plain text password
    public User register(User user) {

        // Check duplicates
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("Username already taken");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }
        if (userRepository.existsByPhoneNumber(user.getPhoneNumber())) {
            throw new IllegalArgumentException("Phone number already registered");
        }
        user.setRole("USER");
        return userRepository.save(user);
    }

    // Login with plain text password
    public Optional<User> login(String username, String password) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent() && user.get().getPassword().equals(password)) {
            System.out.println(" Logged in user: " + user.get().getUsername() + " (" + user.get().getRole() + ")");
            return user;
        } else {
            System.out.println(" Invalid credentials for: " + username);
            return Optional.empty();
        }
    }

    // Login with username OR email
    public Optional<User> loginWithUsernameOrEmail(String usernameOrEmail, String password) {
        Optional<User> user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail);
        if (user.isPresent() && user.get().getPassword().equals(password)) {
            System.out.println(" Logged in user: " + user.get().getUsername() + " (" + user.get().getRole() + ")");
            return user;
        } else {
            System.out.println(" Invalid credentials for: " + usernameOrEmail);
            return Optional.empty();
        }
    }
}
