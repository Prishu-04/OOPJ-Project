package dao;

import db.DBConnection;
import model.Payment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * PaymentDAO - All database operations for the payments table.
 */
public class PaymentDAO {

    // ──────────────────────────────────────────────────────
    // ADD a payment record
    // ──────────────────────────────────────────────────────
    public boolean addPayment(int bookingId, double amount,
                              String paymentDate, String paymentMethod) {
        String sql = "INSERT INTO payments (booking_id, amount, payment_date, " +
                     "payment_method, payment_status) VALUES (?, ?, ?, ?, 'Paid')";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, bookingId);
            ps.setDouble(2, amount);
            ps.setString(3, paymentDate);
            ps.setString(4, paymentMethod);
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Add payment error: " + e.getMessage());
            return false;
        }
    }

    // ──────────────────────────────────────────────────────
    // GET payment by booking ID
    // ──────────────────────────────────────────────────────
    public Payment getPaymentByBookingId(int bookingId) {
        String sql = "SELECT * FROM payments WHERE booking_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, bookingId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Payment(
                    rs.getInt("payment_id"),
                    rs.getInt("booking_id"),
                    rs.getDouble("amount"),
                    rs.getString("payment_date"),
                    rs.getString("payment_method"),
                    rs.getString("payment_status")
                );
            }

        } catch (SQLException e) {
            System.err.println("Get payment error: " + e.getMessage());
        }
        return null;
    }

    // ──────────────────────────────────────────────────────
    // GET ALL payments
    // ──────────────────────────────────────────────────────
    public List<Payment> getAllPayments() {
        List<Payment> list = new ArrayList<>();
        String sql = "SELECT * FROM payments ORDER BY payment_id DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Payment(
                    rs.getInt("payment_id"),
                    rs.getInt("booking_id"),
                    rs.getDouble("amount"),
                    rs.getString("payment_date"),
                    rs.getString("payment_method"),
                    rs.getString("payment_status")
                ));
            }

        } catch (SQLException e) {
            System.err.println("Get all payments error: " + e.getMessage());
        }
        return list;
    }
}
