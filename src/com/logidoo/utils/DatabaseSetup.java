package com.logidoo.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

/**
 * Utility class for initializing database schema from setup.sql file.
 */
public class DatabaseSetup {
    
    public static void initialize(Connection conn) {
        System.out.println("🚀 Checking Database Schema...");
        
        try (Statement stmt = conn.createStatement()) {
            String sqlPath = "setup.sql"; 
            
            if (!Files.exists(Paths.get(sqlPath))) {
                System.out.println("⚠️ setup.sql not found in project root. Skipping Auto-Init.");
                return;
            }

            String sql = new String(Files.readAllBytes(Paths.get(sqlPath)));
            String[] statements = sql.split(";");
            
            for (String query : statements) {
                if (!query.trim().isEmpty()) {
                    try {
                        stmt.execute(query);
                    } catch (Exception e) {
                        // Ignore table already exists errors
                    }
                }
            }
            
            System.out.println("✅ Database Schema Verified!");
            
        } catch (Exception e) {
            System.out.println("❌ Database Init Error: " + e.getMessage());
        }
    }
}
