package com.logidoo.models;

/**
 * Represents a driver in the fleet with license and status information.
 */
public class Driver {
    
    private int id;
    private String name;
    private String licenseNumber;
    private String licenseType;
    private int age;
    private String status;
    private int safetyScore = 100;
    private double hoursDrivenToday = 0.0;
    
    public Driver(String name, String licenseNumber, String licenseType, int age) {
        this.name = name;
        this.licenseNumber = licenseNumber;
        this.licenseType = licenseType;
        this.age = age;
        this.status = "Available";
    }
    
    public Driver(int id, String name, String licenseNumber, String licenseType, int age, String status) {
        this.id = id;
        this.name = name;
        this.licenseNumber = licenseNumber;
        this.licenseType = licenseType;
        this.age = age;
        this.status = status;
    }

    public Driver(int id, String name, String licenseNumber, String licenseType, int age, String status, int safetyScore, double hours) {
        this(id, name, licenseNumber, licenseType, age, status);
        this.safetyScore = safetyScore;
        this.hoursDrivenToday = hours;
    }
    public int getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public String getLicenseNumber() {
        return licenseNumber;
    }
    
    public String getLicenseType() {
        return licenseType;
    }
    
    public int getAge() {
        return age;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    /**
     * Checks if this driver can drive a vehicle requiring the specified license type.
     * Business rules: Heavy > Medium > Light (hierarchical access)
     */
    public boolean canDrive(String requiredLicenseType) {
        if (this.licenseType.equals("Heavy")) {
            return true;
        }
        if (this.licenseType.equals("Medium")) {
            return requiredLicenseType.equals("Medium") || requiredLicenseType.equals("Light");
        }
        if (this.licenseType.equals("Light")) {
            return requiredLicenseType.equals("Light");
        }
        return false;
    }
    
    /**
     * Checks if driver is available considering status and fatigue limits.
     */
    public boolean isAvailable() {
        if (hoursDrivenToday >= 8.0) {
            System.out.println("⛔ Driver " + name + " has exceeded driving limit (8h)!");
            return false;
        }
        return status.equals("Available");
    }

    public int getSafetyScore() { return safetyScore; }
    public double getHoursDrivenToday() { return hoursDrivenToday; }
    
    public void addDrivingHours(double hours) {
        this.hoursDrivenToday += hours;
    }
    
    public void recordIncident() {
        this.safetyScore -= 10;
        if (safetyScore < 0) safetyScore = 0;
    }
}


