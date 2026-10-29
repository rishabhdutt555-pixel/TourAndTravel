package com.tourtravel.repository;

import com.tourtravel.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

// ============================================
// BOOKING REPOSITORY - Database queries for bookings
// ============================================

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    // Get all bookings for a specific user
    List<Booking> findByUserIdOrderByBookingDateDesc(Long userId);

    // Count total bookings
    long count();

    // Sum total revenue from paid bookings
    @Query("SELECT COALESCE(SUM(b.totalAmount), 0) FROM Booking b WHERE b.paymentStatus = 'paid'")
    Long getTotalRevenue();
}
