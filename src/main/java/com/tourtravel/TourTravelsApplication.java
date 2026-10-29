package com.tourtravel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// ============================================
// TOUR & TRAVELS BOOKING SYSTEM
// Java Spring Boot + SQLite Backend
// ============================================

@SpringBootApplication
public class TourTravelsApplication {
    public static void main(String[] args) {
        SpringApplication.run(TourTravelsApplication.class, args);
        System.out.println("\n==========================================");
        System.out.println("  ✈️  Tour & Travels Server Started!");
        System.out.println("  🌐 URL: http://localhost:8080");
        System.out.println("  🗄️  Database: tour_travels.db");
        System.out.println("  👤 Admin: admin / admin123");
        System.out.println("  👤 User:  sarvasav / travel123");
        System.out.println("==========================================\n");
    }
}
