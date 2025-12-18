package com.logidoo.ui;

import com.logidoo.models.InventoryItem;
import com.logidoo.modules.InventoryModule;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Map;

/**
 * Inventory management panel displaying spare parts stock levels.
 * Now reads live inventory from `InventoryModule` so the UI reflects
 * deductions triggered by maintenance tickets.
 */
public class InventoryPanel extends JPanel {
    
    private DefaultTableModel tableModel;
    private InventoryModule inventoryModule;
    
    public InventoryPanel(InventoryModule inventoryModule) {
        this.inventoryModule = inventoryModule;

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("📦 Parts Inventory (Predictive Maintenance)"));
        
        String[] columnNames = {"Part Name", "Quantity", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(tableModel);
        table.setRowHeight(25);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        
        add(new JScrollPane(table), BorderLayout.CENTER);
        refreshData();
    }
    
    public void refreshData() {
        tableModel.setRowCount(0);

        Map<String, InventoryItem> items = inventoryModule.getInventory();
        for (Map.Entry<String, InventoryItem> e : items.entrySet()) {
            InventoryItem it = e.getValue();
            String name = it.getName();
            int qty = it.getQuantity();
            int threshold = it.getLowThreshold();
            String status = (qty <= threshold) ? "⚠️ LOW STOCK" : "✅ OK";
            tableModel.addRow(new Object[]{name, qty, status});
        }
    }
}
