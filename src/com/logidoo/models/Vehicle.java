package com.logidoo.models;

/**
 * Abstract base class for all vehicles in the fleet.
 * Defines common properties and abstract methods that must be implemented by vehicle types.
 */
public abstract class Vehicle {
    
    public enum VehicleStatus {
        MOVING, IDLING, STOPPED, OFFLINE
    }

    private String licensePlate;
    private double mileage;
    private double fuelLevel;
    private String status;
    private VehicleStatus currentStatus = VehicleStatus.STOPPED;
    private double latitude;
    private double longitude;
    
    public Vehicle(String licensePlate, double mileage, double fuelLevel) {
        this.licensePlate = licensePlate;
        this.mileage = mileage;
        this.fuelLevel = fuelLevel;
        this.status = "Available";
        this.latitude = 14.6712;
        this.longitude = -17.4367;
    }
    public String getLicensePlate() {
        return licensePlate;
    }
    
    public double getMileage() {
        return mileage;
    }
    
    public double getFuelLevel() {
        return fuelLevel;
    }
    
    public String getStatus() {
        return status;
    }
    
    public double getLatitude() {
        return latitude;
    }
    
    public double getLongitude() {
        return longitude;
    }
    
    public void setMileage(double mileage) {
        if (mileage >= 0) {
            this.mileage = mileage;
        }
    }
    
    public void setFuelLevel(double fuelLevel) {
        if (fuelLevel >= 0 && fuelLevel <= 100) {
            this.fuelLevel = fuelLevel;
        }
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public void setLocation(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
    
    public abstract double calculateFuelConsumption();
    
    public abstract boolean isHealthy();
    
    public abstract String getRequiredLicenseType();
    
    public abstract String getVehicleType();
}


