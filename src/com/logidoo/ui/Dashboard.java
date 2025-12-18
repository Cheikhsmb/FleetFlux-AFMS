package com.logidoo.ui;

import com.logidoo.dao.DriverDAO;
import com.logidoo.dao.VehicleDAO;
import com.logidoo.models.*;
import com.logidoo.services.FleetManager;
import com.logidoo.modules.InventoryModule;
import com.logidoo.modules.MaintenanceScheduler;
import com.logidoo.models.MaintenanceTicket;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Main dashboard interface for fleet management operations.
 */
public class Dashboard extends JFrame {
    
    private final Color BRAND_COLOR = new Color(255, 140, 0);
    private final Color SUCCESS_COLOR = new Color(34, 139, 34);
    private final Color ERROR_COLOR = new Color(220, 20, 60);
    
    private JTextArea vehicleDisplayArea;
    private JTextArea driverDisplayArea;
    private JTextArea summaryArea;
    
    private JComboBox<String> vehicleTypeCombo;
    private JTextField licensePlateField;
    private JTextField mileageField;
    private JTextField fuelLevelField;
    private JTextField extraInfoField;
    private JLabel extraInfoLabel;
    
    private JTextField driverNameField;
    private JTextField driverLicenseNumberField;
    private JComboBox<String> driverLicenseTypeCombo;
    private JTextField driverAgeField;
    
    private JTextField tripDriverLicenseField;
    private JTextField tripVehiclePlateField;
    
    private VehicleDAO vehicleDAO;
    private DriverDAO driverDAO;
    private FleetManager fleetManager;
    private InventoryModule inventoryModule;
    private MaintenanceScheduler maintenanceScheduler;
    private InventoryPanel inventoryPanel;
    
    public Dashboard() {
        vehicleDAO = new VehicleDAO();
        driverDAO = new DriverDAO();
        fleetManager = new FleetManager();
        // instantiate maintenance and inventory modules
        inventoryModule = new InventoryModule();
        // register inventory module to listen for maintenance tickets
        MaintenanceTicket.addObserver(inventoryModule);
        maintenanceScheduler = new MaintenanceScheduler();
        
        setTitle("FleetFlux - Fleet Management Dashboard");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        checkDatabaseConnection();
        createUI();
        refreshAllData();
    }
    
    private void checkDatabaseConnection() {
        com.logidoo.config.DBConnection.getConnection();
    }
    private void createUI() {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("📊 Overview", createOverviewTab());
        tabbedPane.addTab("🚛 Vehicles", createVehiclesTab());
        tabbedPane.addTab("👤 Drivers", createDriversTab());
        tabbedPane.addTab("🚗 Assign Trip", createTripAssignmentTab());
        tabbedPane.addTab("🌍 Live Map", new LiveMapPanel());
        inventoryPanel = new InventoryPanel(inventoryModule);
        tabbedPane.addTab("📦 Inventory", inventoryPanel);
        add(tabbedPane);
    }

    private JPanel createOverviewTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Fleet Management Overview", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setForeground(BRAND_COLOR);
        panel.add(title, BorderLayout.NORTH);

        summaryArea = new JTextArea();
        summaryArea.setFont(new Font("Courier New", Font.PLAIN, 14));
        summaryArea.setEditable(false);
        summaryArea.setBackground(Color.WHITE);
        summaryArea.setBorder(new TitledBorder("Fleet Summary"));

        JScrollPane scrollPane = new JScrollPane(summaryArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        JButton refreshBtn = new JButton("🔄 Refresh Summary");
        refreshBtn.setBackground(BRAND_COLOR);
        refreshBtn.setForeground(Color.BLACK);
        refreshBtn.addActionListener(e -> refreshSummary());
        panel.add(refreshBtn, BorderLayout.SOUTH);

        return panel;
    }
    
    /**
     * Creates the Vehicles tab for managing vehicles.
     */
    private JPanel createVehiclesTab() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JPanel formPanel = new JPanel(new MigLayout("wrap 2, fillx, insets 20", "[label, right][grow, fill]", "[]15[]"));
        formPanel.setBorder(new TitledBorder("Add New Vehicle"));
        
        formPanel.add(new JLabel("Vehicle Type:"));
        vehicleTypeCombo = new JComboBox<>(new String[]{"Truck", "Van"});
        vehicleTypeCombo.addActionListener(e -> updateExtraInfoLabel());
        formPanel.add(vehicleTypeCombo);
        
        formPanel.add(new JLabel("License Plate:"));
        licensePlateField = new JTextField(20);
        formPanel.add(licensePlateField);
        
        formPanel.add(new JLabel("Mileage (km):"));
        mileageField = new JTextField(20);
        formPanel.add(mileageField);
        
        formPanel.add(new JLabel("Fuel Level (%):"));
        fuelLevelField = new JTextField(20);
        formPanel.add(fuelLevelField);
        
        extraInfoLabel = new JLabel("Max Load (tons):");
        formPanel.add(extraInfoLabel);
        extraInfoField = new JTextField(20);
        formPanel.add(extraInfoField);
        
        JButton addVehicleBtn = new JButton("➕ Add Vehicle");
        addVehicleBtn.setBackground(SUCCESS_COLOR);
        addVehicleBtn.setForeground(Color.BLACK);
        addVehicleBtn.addActionListener(e -> addVehicle());
        formPanel.add(addVehicleBtn, "span 2, grow");
        
        vehicleDisplayArea = new JTextArea();
        vehicleDisplayArea.setFont(new Font("Courier New", Font.PLAIN, 12));
        vehicleDisplayArea.setEditable(false);
        vehicleDisplayArea.setBorder(new TitledBorder("All Vehicles"));
        
        JScrollPane scrollPane = new JScrollPane(vehicleDisplayArea);
        
        JButton refreshBtn = new JButton("🔄 Refresh List");
        refreshBtn.addActionListener(e -> refreshVehicles());
        
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(scrollPane, BorderLayout.CENTER);
        rightPanel.add(refreshBtn, BorderLayout.SOUTH);
        
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, formPanel, rightPanel);
        splitPane.setDividerLocation(400);
        
        mainPanel.add(splitPane, BorderLayout.CENTER);
        
        return mainPanel;
    }
    
    private void updateExtraInfoLabel() {
        String type = (String) vehicleTypeCombo.getSelectedItem();
        
        if (type.equals("Truck")) {
            extraInfoLabel.setText("Max Load (tons):");
        } else if (type.equals("Van")) {
            extraInfoLabel.setText("Passenger Capacity:");
        }
    }
    
    /**
     * Creates the Drivers tab for managing drivers.
     */
    private JPanel createDriversTab() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Left side: Add driver form
        JPanel formPanel = new JPanel(new MigLayout("wrap 2, fillx, insets 20", "[label, right][grow, fill]", "[]15[]"));
        formPanel.setBorder(new TitledBorder("Add New Driver"));
        
        // Name
        formPanel.add(new JLabel("Name:"));
        driverNameField = new JTextField(20);
        formPanel.add(driverNameField);
        
        // License number
        formPanel.add(new JLabel("License Number:"));
        driverLicenseNumberField = new JTextField(20);
        formPanel.add(driverLicenseNumberField);
        
        // License type
        formPanel.add(new JLabel("License Type:"));
        driverLicenseTypeCombo = new JComboBox<>(new String[]{"Light", "Medium", "Heavy"});
        formPanel.add(driverLicenseTypeCombo);
        
        // Age
        formPanel.add(new JLabel("Age:"));
        driverAgeField = new JTextField(20);
        formPanel.add(driverAgeField);
        
        // Add button
        JButton addDriverBtn = new JButton("➕ Add Driver");
        addDriverBtn.setBackground(SUCCESS_COLOR);
        addDriverBtn.setForeground(Color.BLACK);
        addDriverBtn.addActionListener(e -> addDriver());
        formPanel.add(addDriverBtn, "span 2, grow");
        
        // Right side: Driver list
        driverDisplayArea = new JTextArea();
        driverDisplayArea.setFont(new Font("Courier New", Font.PLAIN, 12));
        driverDisplayArea.setEditable(false);
        driverDisplayArea.setBorder(new TitledBorder("All Drivers"));
        
        JScrollPane scrollPane = new JScrollPane(driverDisplayArea);
        
        // Refresh button
        JButton refreshBtn = new JButton("🔄 Refresh List");
        refreshBtn.addActionListener(e -> refreshDrivers());
        
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(scrollPane, BorderLayout.CENTER);
        rightPanel.add(refreshBtn, BorderLayout.SOUTH);
        
        // Split the panel
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, formPanel, rightPanel);
        splitPane.setDividerLocation(400);
        
        mainPanel.add(splitPane, BorderLayout.CENTER);
        
        return mainPanel;
    }
    
    private JPanel createTripAssignmentTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel title = new JLabel("Assign Driver to Vehicle", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setForeground(BRAND_COLOR);
        panel.add(title, BorderLayout.NORTH);
        
        JPanel formPanel = new JPanel(new MigLayout("wrap 2, fillx, insets 20", "[label, right][grow, fill]", "[]15[]"));
        formPanel.setBorder(new TitledBorder("Trip Assignment"));
        
        formPanel.add(new JLabel("Driver License Number:"));
        tripDriverLicenseField = new JTextField(30);
        formPanel.add(tripDriverLicenseField);
        
        formPanel.add(new JLabel("Vehicle License Plate:"));
        tripVehiclePlateField = new JTextField(30);
        formPanel.add(tripVehiclePlateField);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        
        JButton validateBtn = new JButton("✅ Validate Assignment");
        validateBtn.setBackground(BRAND_COLOR);
        validateBtn.setForeground(Color.BLACK);
        validateBtn.addActionListener(e -> validateTrip());
        buttonPanel.add(validateBtn);
        
        JButton assignBtn = new JButton("🚗 Assign Trip");
        assignBtn.setBackground(SUCCESS_COLOR);
        assignBtn.setForeground(Color.BLACK);
        assignBtn.addActionListener(e -> assignTrip());
        buttonPanel.add(assignBtn);
        
        formPanel.add(buttonPanel, "span 2, center");
        
        panel.add(formPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void addVehicle() {
        try {
            String type = (String) vehicleTypeCombo.getSelectedItem();
            String licensePlate = licensePlateField.getText().trim();
            double mileage = Double.parseDouble(mileageField.getText());
            double fuelLevel = Double.parseDouble(fuelLevelField.getText());
            String extraInfo = extraInfoField.getText().trim();
            
            if (licensePlate.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a license plate!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Vehicle vehicle = null;
            if (type.equals("Truck")) {
                double maxLoad = Double.parseDouble(extraInfo);
                vehicle = new Truck(licensePlate, mileage, fuelLevel, maxLoad);
            } else if (type.equals("Van")) {
                int capacity = Integer.parseInt(extraInfo);
                vehicle = new Van(licensePlate, mileage, fuelLevel, capacity);
            }
            
            if (vehicleDAO.addVehicle(vehicle)) {
                JOptionPane.showMessageDialog(this, "✅ Vehicle added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                licensePlateField.setText("");
                mileageField.setText("");
                fuelLevelField.setText("");
                extraInfoField.setText("");
                refreshVehicles();
                refreshSummary();
            } else {
                JOptionPane.showMessageDialog(this, "❌ Failed to add vehicle. Check database connection.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers!", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void addDriver() {
        try {
            String name = driverNameField.getText().trim();
            String licenseNumber = driverLicenseNumberField.getText().trim();
            String licenseType = (String) driverLicenseTypeCombo.getSelectedItem();
            int age = Integer.parseInt(driverAgeField.getText());
            
            if (name.isEmpty() || licenseNumber.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Driver driver = new Driver(name, licenseNumber, licenseType, age);
            
            if (driverDAO.addDriver(driver)) {
                JOptionPane.showMessageDialog(this, "✅ Driver added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                driverNameField.setText("");
                driverLicenseNumberField.setText("");
                driverAgeField.setText("");
                refreshDrivers();
                refreshSummary();
            } else {
                JOptionPane.showMessageDialog(this, "❌ Failed to add driver. Check database connection.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid age!", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void validateTrip() {
        String driverLicense = tripDriverLicenseField.getText().trim();
        String vehiclePlate = tripVehiclePlateField.getText().trim();
        
        if (driverLicense.isEmpty() || vehiclePlate.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both driver license and vehicle plate!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String result = fleetManager.validateTripAssignment(driverLicense, vehiclePlate);
        JOptionPane.showMessageDialog(this, result, "Validation Result", 
            result.startsWith("✅") ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
    }
    
    private void assignTrip() {
        String driverLicense = tripDriverLicenseField.getText().trim();
        String vehiclePlate = tripVehiclePlateField.getText().trim();
        
        if (driverLicense.isEmpty() || vehiclePlate.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both driver license and vehicle plate!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (fleetManager.assignTrip(driverLicense, vehiclePlate)) {
            JOptionPane.showMessageDialog(this, "✅ Trip assigned successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            tripDriverLicenseField.setText("");
            tripVehiclePlateField.setText("");
            refreshAllData();
        } else {
            JOptionPane.showMessageDialog(this, "❌ Failed to assign trip. Please validate first!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void refreshVehicles() {
        var vehicles = vehicleDAO.getAllVehicles();
        StringBuilder sb = new StringBuilder();
        
        if (vehicles.isEmpty()) {
            sb.append("No vehicles in database.\nAdd a vehicle using the form on the left.");
        } else {
            sb.append(String.format("%-15s %-10s %-12s %-10s %-12s %-15s\n", 
                "License Plate", "Type", "Mileage (km)", "Fuel (%)", "Status", "Health"));
            sb.append("────────────────────────────────────────────────────────────────────────────\n");
            
            for (Vehicle v : vehicles) {
                String health = v.isHealthy() ? "✅ Healthy" : "❌ Unhealthy";
                sb.append(String.format("%-15s %-10s %-12.0f %-10.1f %-12s %-15s\n",
                    v.getLicensePlate(), v.getVehicleType(), v.getMileage(), 
                    v.getFuelLevel(), v.getStatus(), health));
            }
        }
        
        vehicleDisplayArea.setText(sb.toString());
    }
    
    private void refreshDrivers() {
        var drivers = driverDAO.getAllDrivers();
        StringBuilder sb = new StringBuilder();
        
        if (drivers.isEmpty()) {
            sb.append("No drivers in database.\nAdd a driver using the form on the left.");
        } else {
            sb.append(String.format("%-20s %-18s %-12s %-6s %-12s\n", 
                "Name", "License Number", "License Type", "Age", "Status"));
            sb.append("──────────────────────────────────────────────────────────────\n");
            
            for (Driver d : drivers) {
                sb.append(String.format("%-20s %-18s %-12s %-6d %-12s\n",
                    d.getName(), d.getLicenseNumber(), d.getLicenseType(), d.getAge(), d.getStatus()));
            }
        }
        
        driverDisplayArea.setText(sb.toString());
    }
    
    private void refreshSummary() {
        String summary = fleetManager.getFleetSummary();
        summaryArea.setText(summary);
    }
    
    private void refreshAllData() {
        // run predictive checks for all vehicles so maintenance tickets (if any) are created
        var vehicles = vehicleDAO.getAllVehicles();
        for (var v : vehicles) {
            maintenanceScheduler.checkVehicleHealth(v);
        }

        refreshVehicles();
        refreshDrivers();
        refreshSummary();
        // refresh inventory UI after possible ticket-triggered deductions
        if (inventoryPanel != null) inventoryPanel.refreshData();
    }
}

