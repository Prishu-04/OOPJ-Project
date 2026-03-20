package dao;

import db.DBConnection;
import model.Room;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * RoomDAO - All database operations for the rooms table.
 */
public class RoomDAO {

    // ──────────────────────────────────────────────────────
    // ADD a new room
    // ──────────────────────────────────────────────────────
    public boolean addRoom(String roomNumber, String roomType,
                           double price, String status) {
        String sql = "INSERT INTO rooms (room_number, room_type, price, status) " +
                     "VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, roomNumber);
            ps.setString(2, roomType);
            ps.setDouble(3, price);
            ps.setString(4, status);
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Add room error: " + e.getMessage());
            return false;
        }
    }

    // ──────────────────────────────────────────────────────
    // UPDATE room details
    // ──────────────────────────────────────────────────────
    public boolean updateRoom(int roomId, String roomNumber, String roomType,
                              double price, String status) {
        String sql = "UPDATE rooms SET room_number=?, room_type=?, price=?, status=? " +
                     "WHERE room_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, roomNumber);
            ps.setString(2, roomType);
            ps.setDouble(3, price);
            ps.setString(4, status);
            ps.setInt(5, roomId);
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Update room error: " + e.getMessage());
            return false;
        }
    }

    // ──────────────────────────────────────────────────────
    // DELETE a room
    // ──────────────────────────────────────────────────────
    public boolean deleteRoom(int roomId) {
        String sql = "DELETE FROM rooms WHERE room_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, roomId);
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Delete room error: " + e.getMessage());
            return false;
        }
    }

    // ──────────────────────────────────────────────────────
    // GET ALL rooms
    // ──────────────────────────────────────────────────────
    public List<Room> getAllRooms() {
        return getRooms("SELECT * FROM rooms ORDER BY room_number", null);
    }

    // ──────────────────────────────────────────────────────
    // GET AVAILABLE rooms only
    // ──────────────────────────────────────────────────────
    public List<Room> getAvailableRooms() {
        return getRooms(
            "SELECT * FROM rooms WHERE status = 'Available' ORDER BY room_number",
            null
        );
    }

    // ──────────────────────────────────────────────────────
    // SEARCH rooms by type
    // ──────────────────────────────────────────────────────
    public List<Room> searchRoomsByType(String roomType) {
        return getRooms(
            "SELECT * FROM rooms WHERE room_type LIKE ? AND status='Available' ORDER BY room_number",
            "%" + roomType + "%"
        );
    }

    // ──────────────────────────────────────────────────────
    // UPDATE room status (Available / Booked)
    // ──────────────────────────────────────────────────────
    public boolean updateRoomStatus(int roomId, String status) {
        String sql = "UPDATE rooms SET status=? WHERE room_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, roomId);
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Update room status error: " + e.getMessage());
            return false;
        }
    }

    // ──────────────────────────────────────────────────────
    // GET room by ID
    // ──────────────────────────────────────────────────────
    public Room getRoomById(int roomId) {
        String sql = "SELECT * FROM rooms WHERE room_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, roomId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
        } catch (SQLException e) {
            System.err.println("Get room by ID error: " + e.getMessage());
        }
        return null;
    }

    // ──────────────────────────────────────────────────────
    // Private helper: execute query and map results
    // ──────────────────────────────────────────────────────
    private List<Room> getRooms(String sql, String param) {
        List<Room> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (param != null) ps.setString(1, param);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }

        } catch (SQLException e) {
            System.err.println("Get rooms error: " + e.getMessage());
        }
        return list;
    }

    private Room mapRow(ResultSet rs) throws SQLException {
        return new Room(
            rs.getInt("room_id"),
            rs.getString("room_number"),
            rs.getString("room_type"),
            rs.getDouble("price"),
            rs.getString("status")
        );
    }
}
