package dao;

import db.DBConnection;
import model.Booking;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * BookingDAO - All database operations for the bookings table.
 */
public class BookingDAO {

    // ──────────────────────────────────────────────────────
    // ADD a new booking; returns generated booking_id or -1
    // ──────────────────────────────────────────────────────
    public int addBooking(int userId, int roomId,
                          String checkIn, String checkOut, double totalAmount) {
        String sql = "INSERT INTO bookings (user_id, room_id, check_in, check_out, " +
                     "total_amount, booking_status) VALUES (?, ?, ?, ?, ?, 'Confirmed')";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql,
                     Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, userId);
            ps.setInt(2, roomId);
            ps.setString(3, checkIn);
            ps.setString(4, checkOut);
            ps.setDouble(5, totalAmount);
            ps.executeUpdate();

            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) return keys.getInt(1);

        } catch (SQLException e) {
            System.err.println("Add booking error: " + e.getMessage());
        }
        return -1;
    }

    // ──────────────────────────────────────────────────────
    // GET bookings for a specific user
    // ──────────────────────────────────────────────────────
    public List<Booking> getBookingsByUser(int userId) {
        List<Booking> list = new ArrayList<>();
        String sql = "SELECT * FROM bookings WHERE user_id=? ORDER BY booking_id DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));

        } catch (SQLException e) {
            System.err.println("Get bookings error: " + e.getMessage());
        }
        return list;
    }

    // ──────────────────────────────────────────────────────
    // GET ALL bookings (admin view)
    // ──────────────────────────────────────────────────────
    public List<Booking> getAllBookings() {
        List<Booking> list = new ArrayList<>();
        String sql = "SELECT * FROM bookings ORDER BY booking_id DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) list.add(mapRow(rs));

        } catch (SQLException e) {
            System.err.println("Get all bookings error: " + e.getMessage());
        }
        return list;
    }

    // ──────────────────────────────────────────────────────
    // GET booking by ID
    // ──────────────────────────────────────────────────────
    public Booking getBookingById(int bookingId) {
        String sql = "SELECT * FROM bookings WHERE booking_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, bookingId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);

        } catch (SQLException e) {
            System.err.println("Get booking by ID error: " + e.getMessage());
        }
        return null;
    }

    // ──────────────────────────────────────────────────────
    // UPDATE booking status
    // ──────────────────────────────────────────────────────
    public boolean updateBookingStatus(int bookingId, String status) {
        String sql = "UPDATE bookings SET booking_status=? WHERE booking_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, bookingId);
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Update booking status error: " + e.getMessage());
            return false;
        }
    }

    // ──────────────────────────────────────────────────────
    // GET ACTIVE bookings by user (Confirmed only)
    // ──────────────────────────────────────────────────────
    public List<Booking> getActiveBookingsByUser(int userId) {
        List<Booking> list = new ArrayList<>();
        String sql = "SELECT * FROM bookings WHERE user_id=? AND booking_status='Confirmed' " +
                     "ORDER BY booking_id DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));

        } catch (SQLException e) {
            System.err.println("Get active bookings error: " + e.getMessage());
        }
        return list;
    }

    // ──────────────────────────────────────────────────────
    // Private helper: map ResultSet row to Booking object
    // ──────────────────────────────────────────────────────
    private Booking mapRow(ResultSet rs) throws SQLException {
        return new Booking(
            rs.getInt("booking_id"),
            rs.getInt("user_id"),
            rs.getInt("room_id"),
            rs.getString("check_in"),
            rs.getString("check_out"),
            rs.getDouble("total_amount"),
            rs.getString("booking_status")
        );
    }
}
