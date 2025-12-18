package com.logidoo.dao;

import com.logidoo.config.DBConnection;
import com.logidoo.models.Driver;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for driver database operations.
 */
public class DriverDAO {
    
    public boolean addDriver(Driver driver) {
        Connection conn = DBConnection.getConnection();
        if (conn == null) {
            System.out.println("⚠️ Database not available - cannot save driver");
            return false;
        }
        
        String sql = "INSERT INTO drivers (name, license_number, license_type, age, status) VALUES (?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, driver.getName());
            stmt.setString(2, driver.getLicenseNumber());
            stmt.setString(3, driver.getLicenseType());
            stmt.setInt(4, driver.getAge());
            stmt.setString(5, driver.getStatus());
            
            return stmt.executeUpdate() > 0;
            
        } catch (Exception e) {
            System.out.println("❌ Error saving driver: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public List<Driver> getAllDrivers() {
        List<Driver> drivers = new ArrayList<>();
        
        Connection conn = DBConnection.getConnection();
        if (conn == null) {
            System.out.println("⚠️ Database not available - returning empty list");
            return drivers;
        }
        
        String sql = "SELECT * FROM drivers";
        
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String licenseNumber = rs.getString("license_number");
                String licenseType = rs.getString("license_type");
                int age = rs.getInt("age");
                String status = rs.getString("status");
                
                Driver driver = new Driver(id, name, licenseNumber, licenseType, age, status);
                drivers.add(driver);
            }
            
        } catch (Exception e) {
            System.out.println("❌ Error loading drivers: " + e.getMessage());
            e.printStackTrace();
        }
        
        return drivers;
    }
    
    public Driver findDriverByLicenseNumber(String licenseNumber) {
        Connection conn = DBConnection.getConnection();
        if (conn == null) {
            return null;
        }
        
        String sql = "SELECT * FROM drivers WHERE license_number = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, licenseNumber);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String licenseType = rs.getString("license_type");
                int age = rs.getInt("age");
                String status = rs.getString("status");
                
                return new Driver(id, name, licenseNumber, licenseType, age, status);
            }
            
        } catch (Exception e) {
            System.out.println("❌ Error finding driver: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    public boolean updateDriverStatus(String licenseNumber, String newStatus) {
        Connection conn = DBConnection.getConnection();
        if (conn == null) {
            return false;
        }
        
        String sql = "UPDATE drivers SET status = ? WHERE license_number = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newStatus);
            stmt.setString(2, licenseNumber);
            return stmt.executeUpdate() > 0;
            
        } catch (Exception e) {
            System.out.println("❌ Error updating driver status: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public List<Driver> getAvailableDrivers() {
        List<Driver> availableDrivers = new ArrayList<>();
        
        Connection conn = DBConnection.getConnection();
        if (conn == null) {
            return availableDrivers;
        }
        
        String sql = "SELECT * FROM drivers WHERE status = 'Available'";
        
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String licenseNumber = rs.getString("license_number");
                String licenseType = rs.getString("license_type");
                int age = rs.getInt("age");
                String status = rs.getString("status");
                
                Driver driver = new Driver(id, name, licenseNumber, licenseType, age, status);
                availableDrivers.add(driver);
            }
            
        } catch (Exception e) {
            System.out.println("❌ Error loading available drivers: " + e.getMessage());
            e.printStackTrace();
        }
        
        return availableDrivers;
    }
}

