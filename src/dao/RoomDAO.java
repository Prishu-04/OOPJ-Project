package dao;

import db.DBConnection;
import model.Room;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * RoomDAO - Handles all database operations for Rooms.
 */
public class RoomDAO implements IDao<Room, Integer> {

    // -------------------------------------------------------
    // ADD
    // -------------------------------------------------------
    @Override
    public boolean add(Room room) {
        String sql = "INSERT INTO rooms (room_number, room_type, price, status) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, room.getRoomNumber());
            ps.setString(2, room.getRoomType());
            ps.setDouble(3, room.getPrice());
            ps.setString(4, room.getStatus());

            return ps.executeUpdate() > 0;

        } catch (SQLIntegrityConstraintViolationException e) {
            // Duplicate room number
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // -------------------------------------------------------
    // GET BY ID
    // -------------------------------------------------------
    @Override
    public Room getById(Integer roomId) {
        String sql = "SELECT * FROM rooms WHERE room_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, roomId);
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
    // GET ALL
    // -------------------------------------------------------
    @Override
    public List<Room> getAll() {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM rooms ORDER BY room_number";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                rooms.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rooms;
    }

    // -------------------------------------------------------
    // GET AVAILABLE ROOMS (for customers)
    // -------------------------------------------------------
    public List<Room> getAvailableRooms() {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM rooms WHERE status = 'Available' ORDER BY room_number";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                rooms.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rooms;
    }

    // -------------------------------------------------------
    // SEARCH by type or status
    // -------------------------------------------------------
    public List<Room> searchRooms(String type, String status) {
        List<Room> rooms = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM rooms WHERE 1=1");

        if (type != null && !type.isEmpty() && !type.equals("All")) {
            sql.append(" AND room_type = ?");
        }
        if (status != null && !status.isEmpty() && !status.equals("All")) {
            sql.append(" AND status = ?");
        }
        sql.append(" ORDER BY room_number");

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int idx = 1;
            if (type != null && !type.isEmpty() && !type.equals("All")) {
                ps.setString(idx++, type);
            }
            if (status != null && !status.isEmpty() && !status.equals("All")) {
                ps.setString(idx, status);
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                rooms.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rooms;
    }

    // -------------------------------------------------------
    // UPDATE
    // -------------------------------------------------------
    @Override
    public boolean update(Room room) {
        String sql = "UPDATE rooms SET room_number=?, room_type=?, price=?, status=? WHERE room_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, room.getRoomNumber());
            ps.setString(2, room.getRoomType());
            ps.setDouble(3, room.getPrice());
            ps.setString(4, room.getStatus());
            ps.setInt(5, room.getRoomId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // -------------------------------------------------------
    // UPDATE STATUS ONLY
    // -------------------------------------------------------
    public boolean updateStatus(int roomId, String status) {
        String sql = "UPDATE rooms SET status=? WHERE room_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, roomId);
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
    public boolean delete(Integer roomId) {
        String sql = "DELETE FROM rooms WHERE room_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, roomId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // -------------------------------------------------------
    // Helper: map ResultSet row to Room object
    // -------------------------------------------------------
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
