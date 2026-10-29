# ✈️ Tour & Travels Booking System — Java Backend

## 🏗️ Tech Stack
| Layer | Technology |
|-------|-----------|
| Frontend | HTML, CSS, Vanilla JS |
| Backend | **Java + Spring Boot** |
| Database | **SQLite** |
| ORM | **Spring Data JPA + Hibernate** |
| Build Tool | **Maven** |

## 📁 Project Structure
```
tour-travels-java/
├── pom.xml                          ← Maven dependencies
├── src/main/java/com/tourtravel/
│   ├── TourTravelsApplication.java  ← Main entry point
│   ├── model/
│   │   ├── User.java                ← User entity (DB table)
│   │   ├── TourPackage.java         ← Package entity (DB table)
│   │   └── Booking.java             ← Booking entity (DB table)
│   ├── repository/
│   │   ├── UserRepository.java      ← User DB queries
│   │   ├── PackageRepository.java   ← Package DB queries
│   │   └── BookingRepository.java   ← Booking DB queries
│   ├── controller/
│   │   ├── AuthController.java      ← Login & Register API
│   │   ├── PackageController.java   ← Packages API
│   │   ├── BookingController.java   ← Bookings API
│   │   └── StatsController.java     ← Admin Stats API
│   └── service/
│       └── DataSeeder.java          ← Seeds initial data
└── src/main/resources/
    ├── application.properties       ← DB config
    └── static/                      ← Frontend files
        ├── index.html
        ├── style.css
        └── script.js
```

## 🚀 How to Run

### Prerequisites
1. **Java 17+** → https://adoptium.net
2. **Maven** → https://maven.apache.org (or use `mvnw` wrapper)

### Steps
```bash
# 1. Go to project folder
cd tour-travels-java

# 2. Build and run
mvn spring-boot:run

# 3. Open browser
# Go to: http://localhost:8080
```

## 🔌 API Endpoints
| Method | URL | Description |
|--------|-----|-------------|
| POST | `/api/auth/login` | Login |
| POST | `/api/auth/register` | Register |
| GET | `/api/packages` | Get all packages |
| POST | `/api/packages` | Add package (admin) |
| DELETE | `/api/packages/{id}` | Delete package (admin) |
| GET | `/api/bookings?userId=X` | Get user bookings |
| POST | `/api/bookings` | Create booking |
| DELETE | `/api/bookings/{id}` | Cancel booking |
| GET | `/api/stats?userId=X` | Admin stats |

## 👤 Login Credentials
| Username | Password | Role |
|----------|----------|------|
| admin | admin123 | Admin |
| sarvasav | travel123 | User |
| nalin | travel123 | User |
| sarthak | travel123 | User |
| rishabh | travel123 | User |
