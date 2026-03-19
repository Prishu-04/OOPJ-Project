package dao;

import db.DBConnection;
import model.Booking;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * BookingDAO - Handles all database operations for Bookings.
 */
public class BookingDAO implements IDao<Booking, Integer> {

    // -------------------------------------------------------
    // ADD - Creates a new booking
    // -------------------------------------------------------
    @Override
    public boolean add(Booking booking) {
        String sql = "INSERT INTO bookings (user_id, room_id, check_in, check_out, total_amount, booking_status) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, booking.getUserId());
            ps.setInt(2, booking.getRoomId());
            ps.setDate(3, booking.getCheckIn());
            ps.setDate(4, booking.getCheckOut());
            ps.setDouble(5, booking.getTotalAmount());
            ps.setString(6, booking.getBookingStatus());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // -------------------------------------------------------
    // ADD and return generated booking ID
    // -------------------------------------------------------
    public int addAndGetId(Booking booking) {
        String sql = "INSERT INTO bookings (user_id, room_id, check_in, check_out, total_amount, booking_status) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, booking.getUserId());
            ps.setInt(2, booking.getRoomId());
            ps.setDate(3, booking.getCheckIn());
            ps.setDate(4, booking.getCheckOut());
            ps.setDouble(5, booking.getTotalAmount());
            ps.setString(6, booking.getBookingStatus());

            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                return keys.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    // -------------------------------------------------------
    // GET BY ID
    // -------------------------------------------------------
    @Override
    public Booking getById(Integer bookingId) {
        String sql = "SELECT b.*, u.name AS user_name, r.room_number, r.room_type "
                   + "FROM bookings b "
                   + "JOIN users u ON b.user_id = u.user_id "
                   + "JOIN rooms r ON b.room_id = r.room_id "
                   + "WHERE b.booking_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, bookingId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // -------------------------------------------------------
    // GET ALL BOOKINGS (Admin view)
    // -------------------------------------------------------
    @Override
    public List<Booking> getAll() {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT b.*, u.name AS user_name, r.room_number, r.room_type "
                   + "FROM bookings b "
                   + "JOIN users u ON b.user_id = u.user_id "
                   + "JOIN rooms r ON b.room_id = r.room_id "
                   + "ORDER BY b.booking_id DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                bookings.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    // -------------------------------------------------------
    // GET BOOKINGS FOR A SPECIFIC USER
    // -------------------------------------------------------
    public List<Booking> getBookingsByUser(int userId) {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT b.*, u.name AS user_name, r.room_number, r.room_type "
                   + "FROM bookings b "
                   + "JOIN users u ON b.user_id = u.user_id "
                   + "JOIN rooms r ON b.room_id = r.room_id "
                   + "WHERE b.user_id = ? ORDER BY b.booking_id DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                bookings.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    // -------------------------------------------------------
    // UPDATE
    // -------------------------------------------------------
    @Override
    public boolean update(Booking booking) {
        String sql = "UPDATE bookings SET user_id=?, room_id=?, check_in=?, check_out=?, "
                   + "total_amount=?, booking_status=? WHERE booking_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, booking.getUserId());
            ps.setInt(2, booking.getRoomId());
            ps.setDate(3, booking.getCheckIn());
            ps.setDate(4, booking.getCheckOut());
            ps.setDouble(5, booking.getTotalAmount());
            ps.setString(6, booking.getBookingStatus());
            ps.setInt(7, booking.getBookingId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // -------------------------------------------------------
    // CANCEL BOOKING (Update status to Cancelled)
    // -------------------------------------------------------
    public boolean cancelBooking(int bookingId) {
        String sql = "UPDATE bookings SET booking_status='Cancelled' WHERE booking_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, bookingId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // -------------------------------------------------------
    // DELETE
    // -------------------------------------------------------
    @Override
    public boolean delete(Integer bookingId) {
        String sql = "DELETE FROM bookings WHERE booking_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, bookingId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // -------------------------------------------------------
    // Helper: map ResultSet row to Booking object
    // -------------------------------------------------------
    private Booking mapRow(ResultSet rs) throws SQLException {
        Booking b = new Booking(
            rs.getInt("booking_id"),
            rs.getInt("user_id"),
            rs.getInt("room_id"),
            rs.getDate("check_in"),
            rs.getDate("check_out"),
            rs.getDouble("total_amount"),
            rs.getString("booking_status")
        );
        // Extra fields from JOIN
        try { b.setUserName(rs.getString("user_name")); }   catch (SQLException ignored) {}
        try { b.setRoomNumber(rs.getString("room_number")); } catch (SQLException ignored) {}
        try { b.setRoomType(rs.getString("room_type")); }   catch (SQLException ignored) {}
        return b;
    }
}
