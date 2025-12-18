package com.logidoo.dao;

import com.logidoo.config.DBConnection;
import com.logidoo.models.Vehicle;
import com.logidoo.models.Truck;
import com.logidoo.models.Van;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for vehicle database operations.
 */
public class VehicleDAO {
    
    public boolean addVehicle(Vehicle vehicle) {
        Connection conn = DBConnection.getConnection();
        if (conn == null) {
            System.out.println("⚠️ Database not available - cannot save vehicle");
            return false;
        }
        
        String sql = "INSERT INTO vehicles (license_plate, vehicle_type, mileage, fuel_level, status, extra_info, latitude, longitude) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, vehicle.getLicensePlate());
            stmt.setString(2, vehicle.getVehicleType());
            stmt.setDouble(3, vehicle.getMileage());
            stmt.setDouble(4, vehicle.getFuelLevel());
            stmt.setString(5, vehicle.getStatus());
            
            String extraInfo = "";
            if (vehicle instanceof Truck) {
                extraInfo = String.valueOf(((Truck) vehicle).getMaxLoadCapacity());
            } else if (vehicle instanceof Van) {
                extraInfo = String.valueOf(((Van) vehicle).getPassengerCapacity());
            }
            stmt.setString(6, extraInfo);
            stmt.setDouble(7, vehicle.getLatitude());
            stmt.setDouble(8, vehicle.getLongitude());
            
            return stmt.executeUpdate() > 0;
            
        } catch (Exception e) {
            System.out.println("❌ Error saving vehicle: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    public List<Vehicle> getAllVehicles() {
        List<Vehicle> vehicles = new ArrayList<>();
        
        Connection conn = DBConnection.getConnection();
        if (conn == null) {
            System.out.println("⚠️ Database not available - returning empty list");
            return vehicles; 
        }
        
        String sql = "SELECT * FROM vehicles";
        
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                String licensePlate = rs.getString("license_plate");
                String vehicleType = rs.getString("vehicle_type");
                double mileage = rs.getDouble("mileage");
                double fuelLevel = rs.getDouble("fuel_level");
                String status = rs.getString("status");
                String extraInfo = rs.getString("extra_info");
                double lat = rs.getDouble("latitude");
                double pid = rs.getDouble("longitude");
                
                Vehicle vehicle = null;
                
                if (vehicleType.equals("Truck")) {
                    double maxLoad = Double.parseDouble(extraInfo);
                    vehicle = new Truck(licensePlate, mileage, fuelLevel, maxLoad);
                } else if (vehicleType.equals("Van")) {
                    int capacity = Integer.parseInt(extraInfo);
                    vehicle = new Van(licensePlate, mileage, fuelLevel, capacity);
                }
                
                if (vehicle != null) {
                    vehicle.setStatus(status);
                    vehicle.setLocation(lat, pid);
                    vehicles.add(vehicle);
                }
            }
            
        } catch (Exception e) {
            System.out.println("❌ Error loading vehicles: " + e.getMessage());
            e.printStackTrace();
        }
        
        return vehicles;
    }
    
    public Vehicle findVehicleByLicensePlate(String licensePlate) {
        Connection conn = DBConnection.getConnection();
        if (conn == null) {
            return null;
        }
        
        String sql = "SELECT * FROM vehicles WHERE license_plate = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, licensePlate);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                String vehicleType = rs.getString("vehicle_type");
                double mileage = rs.getDouble("mileage");
                double fuelLevel = rs.getDouble("fuel_level");
                String status = rs.getString("status");
                String extraInfo = rs.getString("extra_info");
                
                Vehicle vehicle = null;
                if (vehicleType.equals("Truck")) {
                    double maxLoad = Double.parseDouble(extraInfo);
                    vehicle = new Truck(licensePlate, mileage, fuelLevel, maxLoad);
                } else if (vehicleType.equals("Van")) {
                    int capacity = Integer.parseInt(extraInfo);
                    vehicle = new Van(licensePlate, mileage, fuelLevel, capacity);
                }
                
                if (vehicle != null) {
                    vehicle.setStatus(status);
                }
                
                return vehicle;
            }
            
        } catch (Exception e) {
            System.out.println("❌ Error finding vehicle: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    public boolean updateVehicleStatus(String licensePlate, String newStatus) {
        Connection conn = DBConnection.getConnection();
        if (conn == null) {
            return false;
        }
        
        String sql = "UPDATE vehicles SET status = ? WHERE license_plate = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, newStatus);
            stmt.setString(2, licensePlate);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (Exception e) {
            System.out.println("❌ Error updating vehicle status: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}

