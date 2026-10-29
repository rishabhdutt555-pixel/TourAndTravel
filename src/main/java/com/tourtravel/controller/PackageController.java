package com.tourtravel.controller;

import com.tourtravel.model.TourPackage;
import com.tourtravel.model.User;
import com.tourtravel.repository.PackageRepository;
import com.tourtravel.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;

// ============================================
// PACKAGE CONTROLLER
// GET    /api/packages
// GET    /api/packages/{id}
// POST   /api/packages
// DELETE /api/packages/{id}
// ============================================

@RestController
@RequestMapping("/api/packages")
@CrossOrigin(origins = "*")
public class PackageController {

    @Autowired
    private PackageRepository packageRepository;

    @Autowired
    private UserRepository userRepository;

    // ----------------------------------------
    // GET ALL PACKAGES
    // GET /api/packages?search=goa&sort=price
    // ----------------------------------------
    @GetMapping
    public ResponseEntity<List<TourPackage>> getAllPackages(
            @RequestParam(required = false) String search,
            @RequestParam(required = false, defaultValue = "id") String sort) {

        List<TourPackage> packages;

        // If search term provided
        if (search != null && !search.isEmpty()) {
            packages = packageRepository.searchPackages(search);
        } else {
            // Sort based on parameter
            switch (sort) {
                case "price":
                    packages = packageRepository.findAllByOrderByPriceAsc();
                    break;
                case "duration":
                    packages = packageRepository.findAllByOrderByDurationAsc();
                    break;
                case "name":
                    packages = packageRepository.findAllByOrderByNameAsc();
                    break;
                default:
                    packages = packageRepository.findAll();
            }
        }

        return ResponseEntity.ok(packages);
    }

    // ----------------------------------------
    // GET SINGLE PACKAGE
    // GET /api/packages/{id}
    // ----------------------------------------
    @GetMapping("/{id}")
    public ResponseEntity<?> getPackage(@PathVariable Long id) {
        Optional<TourPackage> pkg = packageRepository.findById(id);
        if (pkg.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("error", "Package not found"));
        }
        return ResponseEntity.ok(pkg.get());
    }

    // ----------------------------------------
    // ADD NEW PACKAGE (Admin only)
    // POST /api/packages
    // Body: { "name": "...", "destination": "...", "price": 15000, "duration": 5, ... , "userId": 1 }
    // ----------------------------------------
    @PostMapping
    public ResponseEntity<?> addPackage(@RequestBody Map<String, Object> body) {
        // Check admin access
        Long userId = Long.valueOf(body.get("userId").toString());
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty() || !userOpt.get().getRole().equals("admin")) {
            return ResponseEntity.status(403).body(Map.of("error", "Admin access required"));
        }

        // Validate required fields
        if (body.get("name") == null || body.get("destination") == null ||
            body.get("price") == null || body.get("duration") == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Required fields missing"));
        }

        // Build package object
        TourPackage pkg = new TourPackage();
        pkg.setName(body.get("name").toString());
        pkg.setDestination(body.get("destination").toString());
        pkg.setPrice(Integer.valueOf(body.get("price").toString()));
        pkg.setDuration(Integer.valueOf(body.get("duration").toString()));
        pkg.setDescription(body.getOrDefault("description", "").toString());
        pkg.setImage(body.getOrDefault("image",
            "https://placehold.co/400x250/2563eb/white?text=" + 
            body.get("name").toString().replace(" ", "+")).toString());

        // Save to database
        TourPackage saved = packageRepository.save(pkg);
        return ResponseEntity.status(201).body(saved);
    }

    // ----------------------------------------
    // DELETE PACKAGE (Admin only)
    // DELETE /api/packages/{id}
    // Body: { "userId": 1 }
    // ----------------------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePackage(@PathVariable Long id,
                                           @RequestBody Map<String, Object> body) {
        Long userId = Long.valueOf(body.get("userId").toString());
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty() || !userOpt.get().getRole().equals("admin")) {
            return ResponseEntity.status(403).body(Map.of("error", "Admin access required"));
        }

        if (!packageRepository.existsById(id)) {
            return ResponseEntity.status(404).body(Map.of("error", "Package not found"));
        }

        packageRepository.deleteById(id);
        return ResponseEntity.ok(Map.of("message", "Package deleted successfully"));
    }
}
