package com.logidoo.models;

/**
 * Van vehicle type implementation.
 */
public class Van extends Vehicle {
    
    private int passengerCapacity;
    
    public Van(String licensePlate, double mileage, double fuelLevel, int passengerCapacity) {
        super(licensePlate, mileage, fuelLevel);
        this.passengerCapacity = passengerCapacity;
    }
    
    public int getPassengerCapacity() {
        return passengerCapacity;
    }
    
    @Override
    public double calculateFuelConsumption() {
        return 0.08;
    }
    
    @Override
    public boolean isHealthy() {
        return getMileage() < 150000 && getFuelLevel() > 15;
    }
    
    @Override
    public String getRequiredLicenseType() {
        return "Medium";
    }
    
    @Override
    public String getVehicleType() {
        return "Van";
    }
}


