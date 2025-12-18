package com.logidoo.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Singleton database connection manager.
 * Automatically creates database and schema if they don't exist.
 */
public class DBConnection {
    private static DBConnection instance;
    private Connection connection;

    private final String URL_DB = "jdbc:mysql://localhost:3306/logidoo_enterprise";
    private final String URL_SERVER = "jdbc:mysql://localhost:3306/";
    private final String USER = "root";
    private final String PASS = "";
    
    private static final boolean FORCE_RESET = false;

    private DBConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            if (FORCE_RESET) {
                throw new Exception("Force Reset Enabled");
            }

            this.connection = DriverManager.getConnection(URL_DB, USER, PASS);
            System.out.println("✅ Database Connected Successfully!");
        } catch (Exception e) {
            String msg = FORCE_RESET ? "🔄 Forcing Database Reset..." : "⚠️ Database not found or connection failed. Attempting to create...";
            System.out.println(msg);
            try {
                Connection serverConn = DriverManager.getConnection(URL_SERVER, USER, PASS);
                com.logidoo.utils.DatabaseSetup.initialize(serverConn);
                serverConn.close();
                
                this.connection = DriverManager.getConnection(URL_DB, USER, PASS);
                System.out.println("✅ Database Created and Connected Successfully!");
                
            } catch (Exception ex) {
                System.out.println("❌ Critical Error: Could not create/connect to database.");
                ex.printStackTrace();
                this.connection = null;
            }
        }
    }

    public static Connection getConnection() {
        try {
            if (instance == null) {
                instance = new DBConnection();
            } else if (instance.connection != null && instance.connection.isClosed()) {
                instance = new DBConnection();
            }
        } catch (SQLException e) {
            instance = new DBConnection();
        }
        return instance.connection;
    }
}