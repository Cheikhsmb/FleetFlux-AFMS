package com.logidoo.modules;

import com.logidoo.models.Trip;
import com.logidoo.models.Vehicle;
import com.logidoo.models.Vehicle.VehicleStatus;

/**
 * Handles financial calculations and fraud detection for trips.
 */
public class FinanceManager {
    
    private static final double FUEL_PRICE_PER_LITER = 1.50;
    private static final double DRIVER_HOURLY_WAGE = 20.0;
    
    public double calculateTripCost(Trip trip, double hoursDriven) {
        double fuelCost = trip.getFuelConsumedLiters() * FUEL_PRICE_PER_LITER;
        double wageCost = hoursDriven * DRIVER_HOURLY_WAGE;
        double wearAndTear = trip.getDistanceKm() * 0.10;
        
        double total = fuelCost + wageCost + wearAndTear;
        trip.setTotalCost(total);
        
        return total;
    }
    
    /**
     * Detects potential fuel theft when fuel drops significantly while vehicle is stopped.
     */
    public boolean checkFuelTheft(Vehicle vehicle, double newFuelLevel) {
        double drop = vehicle.getFuelLevel() - newFuelLevel;
        
        if (drop > 5.0 && vehicle.getStatus().equals("Available")) {
            System.out.println("🚨 THEFT ALARM: Fuel level dropped by " + drop + "% while vehicle " + vehicle.getLicensePlate() + " was stopped!");
            return true;
        }
        return false;
    }
}
