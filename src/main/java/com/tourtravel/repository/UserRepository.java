package com.tourtravel.repository;

import com.tourtravel.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

// ============================================
// USER REPOSITORY - Database queries for users
// Spring JPA auto-implements these methods!
// ============================================

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Find user by username (for login)
    Optional<User> findByUsername(String username);

    // Find user by username AND password (login check)
    Optional<User> findByUsernameAndPassword(String username, String password);

    // Check if username already exists (for registration)
    boolean existsByUsername(String username);

    // Check if email already exists (for registration)
    boolean existsByEmail(String email);
}
