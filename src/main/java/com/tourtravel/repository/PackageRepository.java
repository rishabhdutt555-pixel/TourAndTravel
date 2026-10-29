package com.tourtravel.repository;

import com.tourtravel.model.TourPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

// ============================================
// PACKAGE REPOSITORY - Database queries for packages
// ============================================

@Repository
public interface PackageRepository extends JpaRepository<TourPackage, Long> {

    // Search packages by name or destination (case-insensitive)
    @Query("SELECT p FROM TourPackage p WHERE " +
           "LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(p.destination) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<TourPackage> searchPackages(@Param("search") String search);

    // Find packages sorted by price
    List<TourPackage> findAllByOrderByPriceAsc();

    // Find packages sorted by duration
    List<TourPackage> findAllByOrderByDurationAsc();

    // Find packages sorted by name
    List<TourPackage> findAllByOrderByNameAsc();
}
