package dao;

import db.DBConnection;
import model.Admin;
import model.Customer;
import model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * UserDAO - Handles all database operations for users.
 * Uses PreparedStatement to prevent SQL injection.
 */
public class UserDAO {

    // ──────────────────────────────────────────────────────
    // REGISTER a new customer
    // ──────────────────────────────────────────────────────
    public boolean registerUser(String name, String email,
                                String phone, String password) {
        // Check for duplicate email first
        if (emailExists(email)) {
            return false;
        }
        String sql = "INSERT INTO users (name, email, phone, password, role) " +
                     "VALUES (?, ?, ?, ?, 'customer')";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, phone);
            ps.setString(4, password);
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Register error: " + e.getMessage());
            return false;
        }
    }

    // ──────────────────────────────────────────────────────
    // LOGIN - returns User object (Admin or Customer) on success, null on failure
    // ──────────────────────────────────────────────────────
    public User loginUser(String email, String password) {
        String sql = "SELECT * FROM users WHERE email = ? AND password = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int    uid   = rs.getInt("user_id");
                String name  = rs.getString("name");
                String em    = rs.getString("email");
                String ph    = rs.getString("phone");
                String pwd   = rs.getString("password");
                String role  = rs.getString("role");

                if ("admin".equals(role)) {
                    return new Admin(uid, name, em, ph, pwd);
                } else {
                    return new Customer(uid, name, em, ph, pwd);
                }
            }

        } catch (SQLException e) {
            System.err.println("Login error: " + e.getMessage());
        }
        return null;
    }

    // ──────────────────────────────────────────────────────
    // CHECK if email already exists
    // ──────────────────────────────────────────────────────
    public boolean emailExists(String email) {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            return rs.next() && rs.getInt(1) > 0;

        } catch (SQLException e) {
            System.err.println("Email check error: " + e.getMessage());
        }
        return false;
    }

    // ──────────────────────────────────────────────────────
    // GET ALL CUSTOMERS (role = 'customer')
    // ──────────────────────────────────────────────────────
    public List<User> getAllCustomers() {
        List<User> list = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE role = 'customer'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Customer c = new Customer(
                    rs.getInt("user_id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getString("password")
                );
                list.add(c);
            }

        } catch (SQLException e) {
            System.err.println("Get customers error: " + e.getMessage());
        }
        return list;
    }

    // ──────────────────────────────────────────────────────
    // GET USER BY ID
    // ──────────────────────────────────────────────────────
    public User getUserById(int userId) {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String role = rs.getString("role");
                if ("admin".equals(role)) {
                    return new Admin(
                        rs.getInt("user_id"), rs.getString("name"),
                        rs.getString("email"), rs.getString("phone"),
                        rs.getString("password")
                    );
                } else {
                    return new Customer(
                        rs.getInt("user_id"), rs.getString("name"),
                        rs.getString("email"), rs.getString("phone"),
                        rs.getString("password")
                    );
                }
            }

        } catch (SQLException e) {
            System.err.println("Get user by ID error: " + e.getMessage());
        }
        return null;
    }
}
