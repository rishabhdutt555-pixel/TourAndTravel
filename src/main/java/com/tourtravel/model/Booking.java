package com.tourtravel.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import jakarta.persistence.GenerationType;
// ============================================
// BOOKING MODEL - Maps to 'bookings' table
// ============================================

@Entity
@Table(name = "bookings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "package_id", nullable = false)
    private Long packageId;

    @Column(name = "package_name", nullable = false)
    private String packageName;

    @Column(nullable = false)
    private String destination;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    private Integer duration;

    @Column(name = "booking_date")
    private LocalDateTime bookingDate = LocalDateTime.now();

    @Column(name = "travel_date")
    private String travelDate;

    @Column(name = "number_of_people")
    private Integer numberOfPeople = 1;

    @Column(name = "total_amount", nullable = false)
    private Integer totalAmount;

    @Column(nullable = false)
    private String status = "confirmed";

    @Column(name = "payment_status")
    private String paymentStatus = "paid";
}
