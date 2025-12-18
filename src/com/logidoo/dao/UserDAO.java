package com.logidoo.dao;

import com.logidoo.config.DBConnection;
import com.logidoo.models.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Data Access Object for user authentication operations.
 */
public class UserDAO {

    public boolean validateLogin(String username, String password) {
        Connection conn = DBConnection.getConnection();
        if (conn == null) {
            System.out.println("⚠️ Database not available - using demo mode");
            return username.equals("admin") && password.equals("admin123");
        }
        
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            return rs.next();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}