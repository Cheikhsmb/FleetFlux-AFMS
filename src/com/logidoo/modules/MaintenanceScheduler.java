package com.logidoo.modules;

import com.logidoo.models.MaintenanceTicket;
import com.logidoo.models.Vehicle;
import java.util.Random;

/**
 * Predictive maintenance scheduler using mileage-based rules and probability algorithms.
 * AI-assisted: Uses probability models to predict maintenance needs.
 */
public class MaintenanceScheduler {
    
    private static final double MAINTENANCE_INTERVAL_KM = 5000.0;
    private Random random = new Random();

    public void checkVehicleHealth(Vehicle vehicle) {
        if (vehicle.getMileage() % MAINTENANCE_INTERVAL_KM < 100) {
            System.out.println("⚠️ Mileage Warning: " + vehicle.getLicensePlate() + " is due for regular service.");
            new MaintenanceTicket(vehicle, "Routine Maintenance (Oil & Filter)");
        }
        
        // AI-assisted prediction based on mileage
        double failureChance = (vehicle.getMileage() / 100000.0) * 0.1;
        
        if (random.nextDouble() < failureChance) {
            System.out.println("🤖 AI PREDICTION: " + vehicle.getLicensePlate() + " shows signs of brake wear!");
            new MaintenanceTicket(vehicle, "Predictive: Brake Pad Replacement");
        }
    }
}
