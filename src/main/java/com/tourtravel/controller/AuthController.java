package com.tourtravel.controller;

import com.tourtravel.model.User;
import com.tourtravel.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

// ============================================
// AUTH CONTROLLER
// POST /api/auth/login
// POST /api/auth/register
// ============================================

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    // ----------------------------------------
    // LOGIN
    // POST /api/auth/login
    // Body: { "username": "admin", "password": "admin123" }
    // ----------------------------------------
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");

        if (username == null || password == null) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Username and password are required"));
        }

        Optional<User> userOpt = userRepository.findByUsernameAndPassword(username, password);

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(401)
                .body(Map.of("error", "Invalid username or password"));
        }

        User user = userOpt.get();

        // Return user info (without password)
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Login successful");
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("id", user.getId());
        userMap.put("name", user.getName());
        userMap.put("email", user.getEmail());
        userMap.put("phone", user.getPhone());
        userMap.put("username", user.getUsername());
        userMap.put("role", user.getRole());
        response.put("user", userMap);

        return ResponseEntity.ok(response);
    }

    // ----------------------------------------
    // REGISTER
    // POST /api/auth/register
    // Body: { "name": "...", "email": "...", "phone": "...", "username": "...", "password": "..." }
    // ----------------------------------------
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User newUser) {
        // Check required fields
        if (newUser.getName() == null || newUser.getEmail() == null ||
            newUser.getUsername() == null || newUser.getPassword() == null) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "All fields are required"));
        }

        // Check if username already taken
        if (userRepository.existsByUsername(newUser.getUsername())) {
            return ResponseEntity.status(409)
                .body(Map.of("error", "Username already exists"));
        }

        // Check if email already taken
        if (userRepository.existsByEmail(newUser.getEmail())) {
            return ResponseEntity.status(409)
                .body(Map.of("error", "Email already exists"));
        }

        // Set default role
        newUser.setRole("user");

        // Save to database
        User saved = userRepository.save(newUser);

        return ResponseEntity.status(201)
            .body(Map.of("message", "Registration successful", "userId", saved.getId()));
    }
}
