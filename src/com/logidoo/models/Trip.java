package com.logidoo.models;

import java.time.LocalDateTime;

/**
 * Represents a completed trip for financial analysis.
 */
public class Trip {
    private String id;
    private Driver driver;
    private Vehicle vehicle;
    private double distanceKm;
    private double fuelConsumedLiters;
    private double totalCost;
    private LocalDateTime date;

    public Trip(Driver driver, Vehicle vehicle, double distanceKm, double fuelConsumedLiters) {
        this.id = java.util.UUID.randomUUID().toString();
        this.driver = driver;
        this.vehicle = vehicle;
        this.distanceKm = distanceKm;
        this.fuelConsumedLiters = fuelConsumedLiters;
        this.date = LocalDateTime.now();
    }

    public double getDistanceKm() { return distanceKm; }
    public double getFuelConsumedLiters() { return fuelConsumedLiters; }
    public void setTotalCost(double cost) { this.totalCost = cost; }
    public double getTotalCost() { return totalCost; }
}
