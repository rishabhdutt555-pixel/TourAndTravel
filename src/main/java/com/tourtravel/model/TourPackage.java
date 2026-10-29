package com.tourtravel.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import jakarta.persistence.GenerationType;
// ============================================
// PACKAGE MODEL - Maps to 'packages' table
// ============================================

@Entity
@Table(name = "packages")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TourPackage {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String destination;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    private Integer duration;   // in days

    @Column(columnDefinition = "TEXT")
    private String description;

    private String image;

    @Column(name = "available_seats")
    private Integer availableSeats = 20;

    private Double rating = 4.0;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}
