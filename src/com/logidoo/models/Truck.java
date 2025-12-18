package com.logidoo.models;

/**
 * Truck vehicle type implementation.
 */
public class Truck extends Vehicle {
    
    private double maxLoadCapacity;
    
    public Truck(String licensePlate, double mileage, double fuelLevel, double maxLoadCapacity) {
        super(licensePlate, mileage, fuelLevel);
        this.maxLoadCapacity = maxLoadCapacity;
    }
    
    public double getMaxLoadCapacity() {
        return maxLoadCapacity;
    }
    
    @Override
    public double calculateFuelConsumption() {
        return 0.15;
    }
    
    @Override
    public boolean isHealthy() {
        return getMileage() < 200000 && getFuelLevel() > 20;
    }
    
    @Override
    public String getRequiredLicenseType() {
        return "Heavy";
    }
    
    @Override
    public String getVehicleType() {
        return "Truck";
    }
}


