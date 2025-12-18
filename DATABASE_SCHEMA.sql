-- FLEETFLUX - DATABASE SCHEMA
-- 
-- Run this script in your MySQL database BEFORE running the Java application.
-- 
-- Instructions:
-- 1. Open MySQL Workbench or command line
-- 2. Connect to your MySQL server
-- 3. Create the database: CREATE DATABASE logidoo_enterprise;
-- 4. Use the database: USE logidoo_enterprise;
-- 5. Copy and paste this entire script and run it
-- ============================================

-- ============================================
-- TABLE: users
-- ============================================
-- Stores admin users who can log into the system
-- ============================================
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) DEFAULT 'Admin',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert a default admin user (username: admin, password: admin123)
-- In production, you should use encrypted passwords!
INSERT INTO users (username, password, role) 
VALUES ('admin', 'admin123', 'Admin')
ON DUPLICATE KEY UPDATE username = username;

-- ============================================
-- TABLE: vehicles
-- ============================================
-- Stores all vehicles in the fleet (Trucks, Vans, Bikes)
-- ============================================
CREATE TABLE IF NOT EXISTS vehicles (
    id INT AUTO_INCREMENT PRIMARY KEY,
    license_plate VARCHAR(20) UNIQUE NOT NULL,
    vehicle_type VARCHAR(20) NOT NULL,  -- 'Truck', 'Van', or 'Bike'
    mileage DOUBLE NOT NULL DEFAULT 0,
    fuel_level DOUBLE NOT NULL DEFAULT 100,  -- Percentage (0-100)
    status VARCHAR(20) DEFAULT 'Available',  -- 'Available', 'In Use', 'Maintenance'
    extra_info VARCHAR(50),  -- For Truck: max load (tons), Van: passenger capacity, Bike: engine size (CC)
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- ============================================
-- TABLE: drivers
-- ============================================
-- Stores all drivers in the system
-- ============================================
CREATE TABLE IF NOT EXISTS drivers (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    license_number VARCHAR(50) UNIQUE NOT NULL,
    license_type VARCHAR(20) NOT NULL,  -- 'Light', 'Medium', or 'Heavy'
    age INT NOT NULL,
    status VARCHAR(20) DEFAULT 'Available',  -- 'Available', 'On Trip', 'Suspended'
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- ============================================
-- TABLE: trips (Optional - for future expansion)
-- ============================================
-- This table can be used to track trip history
-- ============================================
CREATE TABLE IF NOT EXISTS trips (
    id INT AUTO_INCREMENT PRIMARY KEY,
    driver_license_number VARCHAR(50) NOT NULL,
    vehicle_license_plate VARCHAR(20) NOT NULL,
    start_location VARCHAR(100),
    end_location VARCHAR(100),
    distance_km DOUBLE,
    start_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    end_time TIMESTAMP NULL,
    status VARCHAR(20) DEFAULT 'Active',  -- 'Active', 'Completed', 'Cancelled'
    FOREIGN KEY (driver_license_number) REFERENCES drivers(license_number),
    FOREIGN KEY (vehicle_license_plate) REFERENCES vehicles(license_plate)
);

-- ============================================
-- SAMPLE DATA (Optional - for testing)
-- ============================================
-- Uncomment these lines to add sample data for testing

-- Sample Vehicles
/*
INSERT INTO vehicles (license_plate, vehicle_type, mileage, fuel_level, status, extra_info) VALUES
('TRK-001', 'Truck', 50000, 85, 'Available', '10.5'),
('TRK-002', 'Truck', 150000, 45, 'Available', '15.0'),
('VAN-001', 'Van', 30000, 90, 'Available', '8'),
('VAN-002', 'Van', 120000, 20, 'Maintenance', '12'),
('BIK-001', 'Bike', 20000, 75, 'Available', '125'),
('BIK-002', 'Bike', 95000, 50, 'Available', '250');
*/

-- Sample Drivers
/*
INSERT INTO drivers (name, license_number, license_type, age, status) VALUES
('Amadou Diallo', 'DL-HEAVY-001', 'Heavy', 35, 'Available'),
('Fatou Sarr', 'DL-MED-001', 'Medium', 28, 'Available'),
('Ibrahima Ba', 'DL-LIGHT-001', 'Light', 22, 'Available'),
('Mariama Diop', 'DL-HEAVY-002', 'Heavy', 42, 'Available');
*/

-- ============================================
-- VERIFICATION QUERIES
-- ============================================
-- Run these to verify your tables were created correctly:

-- SELECT * FROM users;
-- SELECT * FROM vehicles;
-- SELECT * FROM drivers;



