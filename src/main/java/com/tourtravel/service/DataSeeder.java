package com.tourtravel.service;

import com.tourtravel.model.TourPackage;
import com.tourtravel.model.User;
import com.tourtravel.repository.PackageRepository;
import com.tourtravel.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

// ============================================
// DATA SEEDER
// Runs automatically on startup
// Seeds default users and packages if DB is empty
// ============================================

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PackageRepository packageRepository;

    @Override
    public void run(String... args) throws Exception {
        seedUsers();
        seedPackages();
    }

    // ----------------------------------------
    // SEED DEFAULT USERS
    // ----------------------------------------
    private void seedUsers() {
        if (userRepository.count() > 0) return; // Already seeded

        userRepository.save(createUser("Admin User", "admin@travel.com", "1234567890", "admin", "admin123", "admin"));
        userRepository.save(createUser("Sarvasav Bhardwaj", "sarvasavbhardwaj@gmail.com", "9876543210", "sarvasav", "travel123", "user"));
        userRepository.save(createUser("Nalin Walia", "walianalin30@gmail.com", "9876543211", "nalin", "travel123", "user"));
        userRepository.save(createUser("Sarthak Rawat", "sarthakr652@gmail.com", "9876543212", "sarthak", "travel123", "user"));
        userRepository.save(createUser("Rishabh Dutt", "rishabhdutt555@gmail.com", "9876543213", "rishabh", "travel123", "user"));

        System.out.println("✅ Users seeded successfully");
    }

    // ----------------------------------------
    // SEED DEFAULT PACKAGES
    // ----------------------------------------
    private void seedPackages() {
        if (packageRepository.count() > 0) return; // Already seeded

        packageRepository.save(createPackage("Goa Beach Paradise", "Goa, India", 15000, 5,
            "Relax on beautiful beaches, enjoy water sports, and experience Goan nightlife. Includes accommodation, breakfast, and sightseeing.",
            "https://images.pexels.com/photos/753626/pexels-photo-753626.jpeg?w=400&h=250&fit=crop", 20, 4.5));

        packageRepository.save(createPackage("Kashmir Great Lakes Trek", "Srinagar, India", 25000, 7,
            "Trek through stunning alpine lakes and meadows in the Himalayas. Professional guide, camping equipment, and meals included.",
            "https://images.pexels.com/photos/3350168/pexels-photo-3350168.jpeg?w=400&h=250&fit=crop", 15, 4.8));

        packageRepository.save(createPackage("Jaipur Heritage Tour", "Jaipur, India", 12000, 4,
            "Explore the Pink City's forts, palaces, and rich cultural heritage. Visit Amer Fort, City Palace, Hawa Mahal, and local markets.",
            "https://images.pexels.com/photos/2082113/pexels-photo-2082113.jpeg?w=400&h=250&fit=crop", 25, 4.3));

        packageRepository.save(createPackage("Kerala Backwaters", "Kerala, India", 18000, 6,
            "Houseboat cruise through serene backwaters and lush greenery. Experience traditional Kerala cuisine and Ayurvedic massage.",
            "https://images.pexels.com/photos/258154/pexels-photo-258154.jpeg?w=400&h=250&fit=crop", 12, 4.7));

        packageRepository.save(createPackage("Manali Adventure", "Manali, India", 10000, 5,
            "Adventure activities in the Himalayas. River rafting, paragliding, trekking, and camping under the stars.",
            "https://images.pexels.com/photos/1293754/pexels-photo-1293754.jpeg?w=400&h=250&fit=crop", 18, 4.6));

        packageRepository.save(createPackage("Varanasi Spiritual Tour", "Varanasi, India", 8000, 3,
            "Experience the spiritual capital of India. Ganga Aarti, temple visits, boat ride, and cultural immersion.",
            "https://images.pexels.com/photos/2559941/pexels-photo-2559941.jpeg?w=400&h=250&fit=crop", 30, 4.4));

        packageRepository.save(createPackage("Dehradun Tour Package", "Dehradun, Uttarakhand", 15000, 6,
            "Explore the beautiful capital of Uttarakhand. Visit Robber's Cave, Sahastradhara, Mindrolling Monastery, and enjoy mountain views.",
            "https://images.pexels.com/photos/2284147/pexels-photo-2284147.jpeg?w=400&h=250&fit=crop", 20, 4.5));

        packageRepository.save(createPackage("Mumbai City Tour", "Mumbai, India", 10000, 4,
            "Experience the city of dreams. Visit Gateway of India, Marine Drive, Bollywood, and enjoy street food.",
            "https://images.pexels.com/photos/1104755/pexels-photo-1104755.jpeg?w=400&h=250&fit=crop", 22, 4.2));

        packageRepository.save(createPackage("Rajasthan Desert Safari", "Jaisalmer, India", 20000, 5,
            "Experience the Thar Desert with camel safari, camping under stars, and cultural performances.",
            "https://images.pexels.com/photos/990752/pexels-photo-990752.jpeg?w=400&h=250&fit=crop", 16, 4.6));

        packageRepository.save(createPackage("Darjeeling Himalayan Tour", "Darjeeling, India", 18000, 6,
            "Visit the queen of hills. Enjoy tea gardens, toy train ride, and stunning Kanchenjunga views.",
            "https://images.pexels.com/photos/189349/pexels-photo-189349.jpeg?w=400&h=250&fit=crop", 20, 4.5));

        packageRepository.save(createPackage("Agra Taj Mahal Tour", "Agra, India", 9000, 3,
            "Visit the iconic Taj Mahal, Agra Fort, and experience Mughal architecture and culture.",
            "https://images.pexels.com/photos/1603650/pexels-photo-1603650.jpeg?w=400&h=250&fit=crop", 35, 4.7));

        packageRepository.save(createPackage("Ladakh Bike Adventure", "Leh, Ladakh", 35000, 10,
            "Epic bike trip through highest motorable roads, Pangong Lake, and Buddhist monasteries.",
            "https://images.pexels.com/photos/3687594/pexels-photo-3687594.jpeg?w=400&h=250&fit=crop", 10, 4.9));

        System.out.println("✅ Packages seeded successfully");
    }

    // Helper: create user object
    private User createUser(String name, String email, String phone,
                             String username, String password, String role) {
        User u = new User();
        u.setName(name);
        u.setEmail(email);
        u.setPhone(phone);
        u.setUsername(username);
        u.setPassword(password);
        u.setRole(role);
        return u;
    }

    // Helper: create package object
    private TourPackage createPackage(String name, String destination, int price,
                                       int duration, String description,
                                       String image, int seats, double rating) {
        TourPackage p = new TourPackage();
        p.setName(name);
        p.setDestination(destination);
        p.setPrice(price);
        p.setDuration(duration);
        p.setDescription(description);
        p.setImage(image);
        p.setAvailableSeats(seats);
        p.setRating(rating);
        return p;
    }
}
