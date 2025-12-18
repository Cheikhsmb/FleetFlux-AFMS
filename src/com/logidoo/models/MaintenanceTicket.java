package com.logidoo.models;

import com.logidoo.observers.MaintenanceObserver;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

/**
 * Maintenance ticket subject in Observer pattern.
 * Notifies observers when created.
 */
public class MaintenanceTicket {
    private String id;
    private Vehicle vehicle;
    private String description;
    private LocalDateTime createdAt;
    private static List<MaintenanceObserver> observers = new ArrayList<>();

    public MaintenanceTicket(Vehicle vehicle, String description) {
        this.id = java.util.UUID.randomUUID().toString();
        this.vehicle = vehicle;
        this.description = description;
        this.createdAt = LocalDateTime.now();
        notifyObservers();
    }
    
    public static void addObserver(MaintenanceObserver observer) {
        observers.add(observer);
    }
    
    public static void removeObserver(MaintenanceObserver observer) {
        observers.remove(observer);
    }
    
    private void notifyObservers() {
        for (MaintenanceObserver observer : observers) {
            observer.onTicketCreated(this);
        }
    }
    
    public String getDescription() { return description; }
    public Vehicle getVehicle() { return vehicle; }
}
