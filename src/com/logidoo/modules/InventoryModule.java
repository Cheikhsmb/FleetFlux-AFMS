package com.logidoo.modules;

import com.logidoo.models.InventoryItem;
import com.logidoo.models.MaintenanceTicket;
import com.logidoo.observers.MaintenanceObserver;
import java.util.HashMap;
import java.util.Map;

/**
 * Inventory management module that observes maintenance tickets.
 * Automatically deducts parts when maintenance tickets are created.
 */
public class InventoryModule implements MaintenanceObserver {
    
    private Map<String, InventoryItem> inventory = new HashMap<>();

    public InventoryModule() {
        inventory.put("Tire", new InventoryItem("Tire", 10, 4));
        inventory.put("Oil Filter", new InventoryItem("Oil Filter", 5, 2));
        inventory.put("Brake Pad", new InventoryItem("Brake Pad", 8, 3));
    }

    /**
     * Expose current inventory map for UI modules.
     */
    public Map<String, InventoryItem> getInventory() {
        return inventory;
    }

    @Override
    public void onTicketCreated(MaintenanceTicket ticket) {
        String desc = ticket.getDescription().toLowerCase();
        
        System.out.println("🔧 InventoryModule received new ticket: " + ticket.getDescription());
        
        if (desc.contains("tire")) {
            deductPart("Tire");
        } else if (desc.contains("oil")) {
            deductPart("Oil Filter");
        } else if (desc.contains("brake")) {
            deductPart("Brake Pad");
        }
    }
    
    private void deductPart(String partName) {
        InventoryItem item = inventory.get(partName);
        if (item != null) {
            item.decreaseQuantity(1);
            System.out.println("✅ Deducted 1 " + partName + " from inventory.");
        } else {
            System.out.println("❌ Part '" + partName + "' not found in inventory!");
        }
    }
}
