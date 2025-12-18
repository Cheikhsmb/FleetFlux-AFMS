package com.logidoo.models;

/**
 * Represents a spare part in inventory with quantity tracking.
 */
public class InventoryItem {
    private String name;
    private int quantity;
    private int minThreshold;

    public InventoryItem(String name, int quantity, int minThreshold) {
        this.name = name;
        this.quantity = quantity;
        this.minThreshold = minThreshold;
    }

    public void decreaseQuantity(int amount) {
        this.quantity -= amount;
        if (this.quantity < minThreshold) {
            System.out.println("⚠️ LOW STOCK ALERT: " + name + " is running low! (" + quantity + " left)");
        }
    }
    
    public String getName() { return name; }
    public int getQuantity() { return quantity; }
    public int getLowThreshold() { return minThreshold; }
}
