package com.example.controller;

import com.example.model.User;
import com.example.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // ===== Signup =====
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerUser(@Valid @RequestBody User user) {
        Map<String, String> response = new HashMap<>();
        try {
            userService.register(user);
            response.put("message", "User registered successfully!");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    // ===== Login =====
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> loginUser(@RequestBody User loginRequest) {
        Optional<User> user = userService.loginWithUsernameOrEmail(
                loginRequest.getUsername(), loginRequest.getPassword());

        Map<String, Object> response = new HashMap<>();
        if (user.isPresent()) {
            String fullName = user.get().getFirstName() + " " + user.get().getLastName();
            response.put("message", "Welcome, " + fullName + "!");
            response.put("role", user.get().getRole());
            response.put("userId", user.get().getId());

            // Add redirect info based on role
            if ("ADMIN".equalsIgnoreCase(user.get().getRole())) {
                response.put("redirect", "ADMIN_DASHBOARD"); // For now placeholder
            } else {
                response.put("redirect", "USER_DASHBOARD");
            }

            return ResponseEntity.ok(response);
        } else {
            response.put("error", "Invalid username/email or password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
}
