package com.logidoo.services;

import com.logidoo.dao.DriverDAO;
import com.logidoo.dao.VehicleDAO;
import com.logidoo.models.Driver;
import com.logidoo.models.Vehicle;

/**
 This is the service layer for the fleet management business logic.
 It Validates trip assignments and manages driver-vehicle relationships.
 */
public class FleetManager {
    
    private DriverDAO driverDAO;
    private VehicleDAO vehicleDAO;
    
    public FleetManager() {
        this.driverDAO = new DriverDAO();
        this.vehicleDAO = new VehicleDAO();
    }
    
    /**
     * Validates if a driver-vehicle assignment is valid.
     * Checks: driver existence, availability, license compatibility, and vehicle health.
     */
    public String validateTripAssignment(String driverLicenseNumber, String vehicleLicensePlate) {
        Driver driver = driverDAO.findDriverByLicenseNumber(driverLicenseNumber);
        if (driver == null) {
            return "❌ ERROR: Driver not found with license: " + driverLicenseNumber;
        }
        
        if (!driver.isAvailable()) {
            return "❌ ERROR: Driver " + driver.getName() + " is not available. Status: " + driver.getStatus();
        }
        
        Vehicle vehicle = vehicleDAO.findVehicleByLicensePlate(vehicleLicensePlate);
        if (vehicle == null) {
            return "❌ ERROR: Vehicle not found with license plate: " + vehicleLicensePlate;
        }
        
        if (!vehicle.getStatus().equals("Available")) {
            return "❌ ERROR: Vehicle " + vehicleLicensePlate + " is not available. Status: " + vehicle.getStatus();
        }
        
        String requiredLicense = vehicle.getRequiredLicenseType();
        if (!driver.canDrive(requiredLicense)) {
            return "❌ ERROR: License mismatch!\n" +
                   "Driver " + driver.getName() + " has " + driver.getLicenseType() + " license.\n" +
                   "Vehicle " + vehicleLicensePlate + " requires " + requiredLicense + " license.";
        }
        
        if (!vehicle.isHealthy()) {
            return "❌ ERROR: Vehicle " + vehicleLicensePlate + " is not healthy!\n" +
                   "Mileage: " + vehicle.getMileage() + " km\n" +
                   "Fuel Level: " + vehicle.getFuelLevel() + "%\n" +
                   "This vehicle needs maintenance before it can be used.";
        }
        
        return "✅ VALID ASSIGNMENT!\n" +
               "Driver: " + driver.getName() + " (" + driver.getLicenseType() + " license)\n" +
               "Vehicle: " + vehicleLicensePlate + " (" + vehicle.getVehicleType() + ")\n" +
               "Mileage: " + vehicle.getMileage() + " km\n" +
               "Fuel: " + vehicle.getFuelLevel() + "%\n" +
               "Ready for trip assignment!";
    }
    
    /**
     * Assigns a trip by updating driver and vehicle status.
     * Should only be called after validateTripAssignment() returns success.
     */
    public boolean assignTrip(String driverLicenseNumber, String vehicleLicensePlate) {
        String validation = validateTripAssignment(driverLicenseNumber, vehicleLicensePlate);
        if (!validation.startsWith("✅")) {
            return false;
        }
        
        boolean driverUpdated = driverDAO.updateDriverStatus(driverLicenseNumber, "On Trip");
        boolean vehicleUpdated = vehicleDAO.updateVehicleStatus(vehicleLicensePlate, "In Use");
        return driverUpdated && vehicleUpdated;
    }
    
    public boolean completeTrip(String driverLicenseNumber, String vehicleLicensePlate) {
        boolean driverUpdated = driverDAO.updateDriverStatus(driverLicenseNumber, "Available");
        boolean vehicleUpdated = vehicleDAO.updateVehicleStatus(vehicleLicensePlate, "Available");
        return driverUpdated && vehicleUpdated;
    }
    
    /**
     * Returns a summary of fleet status with statistics.
     */
    public String getFleetSummary() {
        // Get all vehicles and drivers
        var vehicles = vehicleDAO.getAllVehicles();
        var drivers = driverDAO.getAllDrivers();
        
        // Count by status
        int availableVehicles = 0;
        int inUseVehicles = 0;
        int maintenanceVehicles = 0;
        
        for (Vehicle v : vehicles) {
            if (v.getStatus().equals("Available")) availableVehicles++;
            else if (v.getStatus().equals("In Use")) inUseVehicles++;
            else if (v.getStatus().equals("Maintenance")) maintenanceVehicles++;
        }
        
        int availableDrivers = 0;
        int onTripDrivers = 0;
        
        for (Driver d : drivers) {
            if (d.getStatus().equals("Available")) availableDrivers++;
            else if (d.getStatus().equals("On Trip")) onTripDrivers++;
        }
        
        return String.format(
            "📊 FLEET SUMMARY\n" +
            "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n" +
            "Vehicles: %d total\n" +
            "  ✅ Available: %d\n" +
            "  🚛 In Use: %d\n" +
            "  🔧 Maintenance: %d\n\n" +
            "Drivers: %d total\n" +
            "  ✅ Available: %d\n" +
            "  🚗 On Trip: %d",
            vehicles.size(), availableVehicles, inUseVehicles, maintenanceVehicles,
            drivers.size(), availableDrivers, onTripDrivers
        );
    }
}


