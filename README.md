# 🚛 FleetFlux - Advanced Fleet Management System

## 📋 Project Overview

This is an **Advanced Fleet Management System** built for an OOP (Object-Oriented Programming) class. The system manages a fleet of vehicles (Trucks, Vans, and Bikes) and drivers across West Africa, ensuring safe and efficient trip assignments.

## 🎯 The Problem It Solves

**Without this system:** FleetFlux might accidentally send a driver with a Light license to drive a Truck on a 1000km trip to Mali, or send a vehicle that's about to break down.

**With this system:** The software acts as a "Filter" that only allows valid combinations:
- ✅ Right Driver (correct license type)
- ✅ Right Vehicle (appropriate for the trip)
- ✅ Healthy Vehicle (low mileage, enough fuel)

## 🏗️ Architecture (Layered Design)

The project follows a **professional layered architecture**:

```
┌─────────────────────────────────────┐
│         UI Layer (The Face)         │  ← LoginScreen, Dashboard
├─────────────────────────────────────┤
│      Service Layer (The Brain)     │  ← FleetManager (Business Rules)
├─────────────────────────────────────┤
│        DAO Layer (The Memory)       │  ← VehicleDAO, DriverDAO (Database)
├─────────────────────────────────────┤
│      Model Layer (The Blueprint)    │  ← Vehicle, Driver (Data Structures)
├─────────────────────────────────────┤
│      Config Layer (The Setup)       │  ← DBConnection (Database Config)
└─────────────────────────────────────┘
```

## 🧩 OOP Principles Demonstrated

### 1. **Abstraction**
- `Vehicle` is an abstract class that defines what ALL vehicles must have
- You can't create a "Vehicle" directly - you must create a specific type (Truck, Van, Bike)

### 2. **Inheritance**
- `Truck`, `Van`, and `Bike` all inherit from `Vehicle`
- They get all the common properties (license plate, mileage, fuel) for free
- They only add what makes them special

### 3. **Encapsulation**
- All fields are `private` - can't be accessed directly
- Must use getters/setters (the "gatekeepers")
- Prevents mistakes like setting mileage to -1000

### 4. **Polymorphism**
- A list can hold different vehicle types: `List<Vehicle>`
- When you call `vehicle.calculateFuelConsumption()`, Java automatically uses:
  - Truck logic for trucks
  - Van logic for vans
  - Bike logic for bikes

## 📁 Project Structure

```
FleetFlux/
├── src/
│   └── com/
│       └── logidoo/
│           ├── config/
│           │   └── DBConnection.java          # Database connection (Singleton)
│           ├── dao/
│           │   ├── UserDAO.java              # User database operations
│           │   ├── VehicleDAO.java           # Vehicle database operations
│           │   └── DriverDAO.java            # Driver database operations
│           ├── models/
│           │   ├── User.java                 # User model
│           │   ├── Vehicle.java              # Abstract vehicle class
│           │   ├── Truck.java                # Truck (extends Vehicle)
│           │   ├── Van.java                  # Van (extends Vehicle)
│           │   ├── Bike.java                 # Bike (extends Vehicle)
│           │   └── Driver.java               # Driver model
│           ├── services/
│           │   ├── FleetManager.java         # Business logic (THE BRAIN)
│           │   └── LogidoLogo.png            # FleetFlux logo image (original filename preserved)
│           └── ui/
│               ├── LoginScreen.java           # Login window
│               └── Dashboard.java             # Main control center
├── DATABASE_SCHEMA.sql                        # Database setup script
└── README.md                                  # This file
```

## 🚀 Getting Started

### Prerequisites

1. **Java JDK 8 or higher**
2. **MySQL Server** (version 5.7 or higher)
3. **MySQL JDBC Driver** (mysql-connector-java-8.0.x.jar)
4. **IDE** (IntelliJ IDEA, Eclipse, or VS Code)

### Setup Instructions

#### Step 1: Database Setup

1. Open MySQL Workbench or command line
2. Create the database:
   ```sql
   CREATE DATABASE logidoo_enterprise;
   ```
3. Run the `DATABASE_SCHEMA.sql` script to create all tables
4. Update database credentials in `DBConnection.java` if needed:
   ```java
   private final String URL = "jdbc:mysql://localhost:3306/logidoo_enterprise";
   private final String USER = "root";
   private final String PASS = "your_password";
   ```

#### Step 2: Add MySQL JDBC Driver

1. Download `mysql-connector-java-8.0.x.jar` from MySQL website
2. Add it to your project's classpath:
   - **IntelliJ IDEA:** File → Project Structure → Libraries → Add JAR
   - **Eclipse:** Right-click project → Build Path → Add External JARs

#### Step 3: Run the Application

1. Open `LoginScreen.java`
2. Run the `main` method
3. Login with:
   - **Username:** `admin`
   - **Password:** `admin123`

## 🎮 How to Use

### 1. Login
- Enter username and password
- Click "LOGIN" or press Enter

### 2. Dashboard Overview
- View fleet summary (total vehicles, drivers, availability)

### 3. Manage Vehicles
- **Add Vehicle:** Fill in the form (type, license plate, mileage, fuel, etc.)
- **View Vehicles:** See all vehicles with their status and health

### 4. Manage Drivers
- **Add Driver:** Fill in name, license number, license type, age
- **View Drivers:** See all drivers with their license types and status

### 5. Assign Trips
- Enter driver license number and vehicle license plate
- Click "Validate Assignment" to check if it's valid
- Click "Assign Trip" to actually assign (updates status to "In Use" / "On Trip")

## 🔍 Key Features

### Vehicle Management
- ✅ Add Trucks, Vans, and Bikes
- ✅ Track mileage and fuel levels
- ✅ Check vehicle health automatically
- ✅ View all vehicles with status

### Driver Management
- ✅ Add drivers with license information
- ✅ Track driver availability
- ✅ License type validation (Light, Medium, Heavy)

### Trip Assignment Validation
- ✅ Checks if driver exists and is available
- ✅ Checks if vehicle exists and is available
- ✅ Validates license type compatibility
- ✅ Validates vehicle health (mileage, fuel)
- ✅ Prevents invalid assignments

## 📊 Database Schema

### Tables

1. **users** - Admin users for login
2. **vehicles** - All vehicles (Trucks, Vans, Bikes)
3. **drivers** - All drivers with license information
4. **trips** - Trip history (optional, for future expansion)

See `DATABASE_SCHEMA.sql` for complete schema details.

## 💡 Code Philosophy

Every important part of the code has **clear comments** explaining:
- **What** it does
- **Why** it's needed
- **How** it works

The code is written to be **simple and understandable** for learning purposes.

## 🎓 Learning Objectives Achieved

- ✅ Object-Oriented Programming (OOP) principles
- ✅ Layered Architecture design
- ✅ Database integration (JDBC)
- ✅ GUI development (Java Swing)
- ✅ Business logic implementation
- ✅ Error handling and validation

## 📝 Notes

- The system uses **prepared statements** to prevent SQL injection
- Passwords are stored in plain text (for simplicity) - in production, use encryption!
- The database connection uses a **Singleton pattern** to ensure only one connection

## 🔮 Future Enhancements

- GPS tracking integration
- Fuel sensor integration
- Salary tracking for drivers
- Trip history and reporting
- Email notifications
- Mobile app integration

## 👨‍💻 Author

Built by CABS for OOP final class project - Advanced Fleet Management System

---

**Remember:** This system prevents chaos by ensuring only valid driver-vehicle combinations can start trips! 🚛✨



