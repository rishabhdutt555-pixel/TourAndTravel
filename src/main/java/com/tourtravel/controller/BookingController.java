package com.tourtravel.controller;

import com.tourtravel.model.Booking;
import com.tourtravel.model.TourPackage;
import com.tourtravel.model.User;
import com.tourtravel.repository.BookingRepository;
import com.tourtravel.repository.PackageRepository;
import com.tourtravel.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;

// ============================================
// BOOKING CONTROLLER
// GET    /api/bookings?userId=1
// GET    /api/bookings/all?userId=1  (admin)
// POST   /api/bookings
// DELETE /api/bookings/{id}
// ============================================

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "*")
public class BookingController {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private PackageRepository packageRepository;

    @Autowired
    private UserRepository userRepository;

    // ----------------------------------------
    // GET USER'S BOOKINGS
    // GET /api/bookings?userId=1
    // ----------------------------------------
    @GetMapping
    public ResponseEntity<?> getUserBookings(@RequestParam Long userId) {
        List<Booking> bookings = bookingRepository.findByUserIdOrderByBookingDateDesc(userId);
        return ResponseEntity.ok(bookings);
    }

    // ----------------------------------------
    // GET ALL BOOKINGS (Admin only)
    // GET /api/bookings/all?userId=1
    // ----------------------------------------
    @GetMapping("/all")
    public ResponseEntity<?> getAllBookings(@RequestParam Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty() || !userOpt.get().getRole().equals("admin")) {
            return ResponseEntity.status(403).body(Map.of("error", "Admin access required"));
        }
        List<Booking> bookings = bookingRepository.findAll();
        return ResponseEntity.ok(bookings);
    }

    // ----------------------------------------
    // CREATE BOOKING
    // POST /api/bookings
    // Body: { "userId": 1, "packageId": 2, "numberOfPeople": 2, "travelDate": "2025-06-15" }
    // ----------------------------------------
    @PostMapping
    public ResponseEntity<?> createBooking(@RequestBody Map<String, Object> body) {
        // Validate required fields
        if (body.get("userId") == null || body.get("packageId") == null) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "userId and packageId are required"));
        }

        Long userId = Long.valueOf(body.get("userId").toString());
        Long packageId = Long.valueOf(body.get("packageId").toString());
        int numberOfPeople = body.get("numberOfPeople") != null ?
            Integer.parseInt(body.get("numberOfPeople").toString()) : 1;

        // Check package exists
        Optional<TourPackage> pkgOpt = packageRepository.findById(packageId);
        if (pkgOpt.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("error", "Package not found"));
        }

        TourPackage pkg = pkgOpt.get();
        int totalAmount = pkg.getPrice() * numberOfPeople;

        // Create booking
        Booking booking = new Booking();
        booking.setUserId(userId);
        booking.setPackageId(packageId);
        booking.setPackageName(pkg.getName());
        booking.setDestination(pkg.getDestination());
        booking.setPrice(pkg.getPrice());
        booking.setDuration(pkg.getDuration());
        booking.setNumberOfPeople(numberOfPeople);
        booking.setTotalAmount(totalAmount);
        booking.setStatus("confirmed");
        booking.setPaymentStatus("paid");

        if (body.get("travelDate") != null) {
            booking.setTravelDate(body.get("travelDate").toString());
        }

        // Save to database
        Booking saved = bookingRepository.save(booking);
        return ResponseEntity.status(201).body(saved);
    }

    // ----------------------------------------
    // CANCEL BOOKING
    // DELETE /api/bookings/{id}
    // Body: { "userId": 1 }
    // ----------------------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelBooking(@PathVariable Long id,
                                           @RequestBody Map<String, Object> body) {
        Long userId = Long.valueOf(body.get("userId").toString());

        Optional<Booking> bookingOpt = bookingRepository.findById(id);
        if (bookingOpt.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("error", "Booking not found"));
        }

        Booking booking = bookingOpt.get();

        // Only owner or admin can cancel
        Optional<User> userOpt = userRepository.findById(userId);
        boolean isAdmin = userOpt.isPresent() && userOpt.get().getRole().equals("admin");
        boolean isOwner = booking.getUserId().equals(userId);

        if (!isOwner && !isAdmin) {
            return ResponseEntity.status(403).body(Map.of("error", "Not authorized"));
        }

        bookingRepository.deleteById(id);
        return ResponseEntity.ok(Map.of("message", "Booking cancelled successfully"));
    }
}
