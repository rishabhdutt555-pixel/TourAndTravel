package com.tourtravel.controller;

import com.tourtravel.model.User;
import com.tourtravel.repository.BookingRepository;
import com.tourtravel.repository.PackageRepository;
import com.tourtravel.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

// ============================================
// STATS CONTROLLER (Admin Dashboard)
// GET /api/stats?userId=1
// ============================================

@RestController
@RequestMapping("/api/stats")
@CrossOrigin(origins = "*")
public class StatsController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PackageRepository packageRepository;

    @Autowired
    private BookingRepository bookingRepository;

    // GET /api/stats?userId=1
    @GetMapping
    public ResponseEntity<?> getStats(@RequestParam Long userId) {
        // Check admin
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty() || !userOpt.get().getRole().equals("admin")) {
            return ResponseEntity.status(403).body(Map.of("error", "Admin access required"));
        }

        // Collect stats from database
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsers", userRepository.count());
        stats.put("totalPackages", packageRepository.count());
        stats.put("totalBookings", bookingRepository.count());
        stats.put("totalRevenue", bookingRepository.getTotalRevenue());

        return ResponseEntity.ok(stats);
    }
}
