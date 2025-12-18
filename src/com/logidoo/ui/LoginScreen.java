package com.logidoo.ui;

import com.logidoo.dao.UserDAO;
import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.net.URL;

public class LoginScreen extends JFrame {

    private final Color BRAND_COLOR = new Color(255, 140, 0);
    private final Color TEXT_COLOR = new Color(60, 60, 60);

    public LoginScreen() {
        setTitle("FleetFlux System Access");
        setSize(300, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        JPanel topPanel = new JPanel();
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(new EmptyBorder(30, 0, 15, 0));

        JLabel logoLabel = new JLabel();

        try {
            URL imgUrl = getClass().getResource("/com/logidoo/services/LogidoLogo.png");
            if (imgUrl != null) {
                ImageIcon icon = new ImageIcon(imgUrl);
                Image img = icon.getImage().getScaledInstance(100, -1, Image.SCALE_SMOOTH);
                logoLabel.setIcon(new ImageIcon(img));
            } else {
                logoLabel.setText("FLEETFLUX");
                logoLabel.setFont(new Font("Arial", Font.BOLD, 24));
                logoLabel.setForeground(BRAND_COLOR);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        topPanel.add(logoLabel);
        add(topPanel, BorderLayout.NORTH);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(3, 1, 15, 15));
        inputPanel.setBackground(Color.WHITE);
        inputPanel.setBorder(new EmptyBorder(20, 50, 20, 50));

        JTextField userField = new JTextField();
        userField.setBorder(BorderFactory.createTitledBorder("Username"));
        userField.setFont(new Font("Arial", Font.PLAIN, 14));

        JPasswordField passField = new JPasswordField();
        passField.setBorder(BorderFactory.createTitledBorder("Password"));
        passField.setFont(new Font("Arial", Font.PLAIN, 14));

        JButton loginButton = new JButton("LOGIN");
        loginButton.setBackground(BRAND_COLOR);
        loginButton.setForeground(new Color(229, 229, 229));
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setFocusPainted(false);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        inputPanel.add(userField);
        inputPanel.add(passField);
        inputPanel.add(loginButton);

        add(inputPanel, BorderLayout.CENTER);

        loginButton.addActionListener(e -> {
            String username = userField.getText();
            String password = new String(passField.getPassword());

            UserDAO dao = new UserDAO();
            boolean isValid = dao.validateLogin(username, password);

            if (isValid) {
                JOptionPane.showMessageDialog(this, "✅ Welcome back, Admin!");
                dispose();
                new Dashboard().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Invalid credentials! Please try again!",
                        "Security Alert",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        getRootPane().setDefaultButton(loginButton);
    }

    public static void main(String[] args) {
        try { 
            FlatLightLaf.setup();
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> new LoginScreen().setVisible(true));
    }
}