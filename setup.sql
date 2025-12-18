DROP DATABASE IF EXISTS logidoo_enterprise;
CREATE DATABASE logidoo_enterprise;
USE logidoo_enterprise;

-- 1. Vehicles Table
CREATE TABLE IF NOT EXISTS vehicles (
    license_plate VARCHAR(20) PRIMARY KEY,
    vehicle_type VARCHAR(20),
    mileage DOUBLE,
    fuel_level DOUBLE,
    status VARCHAR(20),
    extra_info VARCHAR(50),
    latitude DOUBLE DEFAULT 14.6712, 
    longitude DOUBLE DEFAULT -17.4367
);

-- 2. Drivers Table
CREATE TABLE IF NOT EXISTS drivers (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    license_number VARCHAR(50) UNIQUE,
    license_type VARCHAR(20),
    age INT,
    status VARCHAR(20),
    safety_score INT DEFAULT 100,
    hours_driven_today DOUBLE DEFAULT 0.0
);

-- 3. Users Table (for Auth)
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE,
    password VARCHAR(50)
);

-- 4. Inventory Table (for Predictive Maintenance)
CREATE TABLE IF NOT EXISTS inventory (
    id INT AUTO_INCREMENT PRIMARY KEY,
    part_name VARCHAR(100),
    quantity INT,
    min_threshold INT -- Alert if quantity drops below this
);

-- 5. Maintenance Tickets Table
CREATE TABLE IF NOT EXISTS maintenance_tickets (
    id INT AUTO_INCREMENT PRIMARY KEY,
    vehicle_license VARCHAR(20),
    description TEXT,
    status VARCHAR(20), -- 'Scheduled', 'In Progress', 'Completed'
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (vehicle_license) REFERENCES vehicles(license_plate)
);

-- 6. Trips Table (for Financials)
CREATE TABLE IF NOT EXISTS trips (
    id INT AUTO_INCREMENT PRIMARY KEY,
    driver_id INT,
    vehicle_license VARCHAR(20),
    distance_km DOUBLE,
    fuel_consumed DOUBLE,
    total_cost DOUBLE,
    trip_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (driver_id) REFERENCES drivers(id),
    FOREIGN KEY (vehicle_license) REFERENCES vehicles(license_plate)
);

-- Initial Data Seeding
INSERT IGNORE INTO users (username, password) VALUES ('admin', 'admin123');
INSERT IGNORE INTO inventory (part_name, quantity, min_threshold) VALUES 
('Tire (Truck)', 10, 4),
('Oil Filter', 20, 5),
('Brake Pad', 15, 4);
