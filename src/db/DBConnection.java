package db;

import java.sql.*;

/**
 * DBConnection - Manages SQLite database connection.
 * Creates tables and inserts default data on first run.
 */
public class DBConnection {

    private static final String URL = "jdbc:sqlite:hotel.db";
    private static Connection connection = null;

    // Returns a single shared connection (singleton pattern)
    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName("org.sqlite.JDBC");
                connection = DriverManager.getConnection(URL);
                connection.setAutoCommit(true);
            }
        } catch (Exception e) {
            System.err.println("Connection error: " + e.getMessage());
            e.printStackTrace();
        }
        return connection;
    }

    // Creates all tables and inserts default records if not present
    public static void initializeDatabase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            // ---------- USERS TABLE ----------
            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS users (" +
                "  user_id  INTEGER PRIMARY KEY AUTOINCREMENT," +
                "  name     TEXT    NOT NULL," +
                "  email    TEXT    UNIQUE NOT NULL," +
                "  phone    TEXT," +
                "  password TEXT    NOT NULL," +
                "  role     TEXT    DEFAULT 'customer'" +
                ")"
            );

            // ---------- ROOMS TABLE ----------
            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS rooms (" +
                "  room_id     INTEGER PRIMARY KEY AUTOINCREMENT," +
                "  room_number TEXT UNIQUE NOT NULL," +
                "  room_type   TEXT NOT NULL," +
                "  price       REAL NOT NULL," +
                "  status      TEXT DEFAULT 'Available'" +
                ")"
            );

            // ---------- BOOKINGS TABLE ----------
            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS bookings (" +
                "  booking_id     INTEGER PRIMARY KEY AUTOINCREMENT," +
                "  user_id        INTEGER," +
                "  room_id        INTEGER," +
                "  check_in       TEXT NOT NULL," +
                "  check_out      TEXT NOT NULL," +
                "  total_amount   REAL," +
                "  booking_status TEXT DEFAULT 'Confirmed'," +
                "  FOREIGN KEY(user_id) REFERENCES users(user_id)," +
                "  FOREIGN KEY(room_id) REFERENCES rooms(room_id)" +
                ")"
            );

            // ---------- PAYMENTS TABLE ----------
            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS payments (" +
                "  payment_id     INTEGER PRIMARY KEY AUTOINCREMENT," +
                "  booking_id     INTEGER," +
                "  amount         REAL," +
                "  payment_date   TEXT," +
                "  payment_method TEXT," +
                "  payment_status TEXT DEFAULT 'Paid'," +
                "  FOREIGN KEY(booking_id) REFERENCES bookings(booking_id)" +
                ")"
            );

            // ---------- DEFAULT ADMIN ----------
            ResultSet rs = stmt.executeQuery(
                "SELECT COUNT(*) AS cnt FROM users WHERE role = 'admin'"
            );
            if (rs.next() && rs.getInt("cnt") == 0) {
                stmt.executeUpdate(
                    "INSERT INTO users (name, email, phone, password, role) " +
                    "VALUES ('Administrator', 'admin@hotel.com', '9999999999', 'admin123', 'admin')"
                );
                System.out.println("Default admin inserted.");
            }
            rs.close();

            // ---------- SAMPLE ROOMS ----------
            rs = stmt.executeQuery("SELECT COUNT(*) AS cnt FROM rooms");
            if (rs.next() && rs.getInt("cnt") == 0) {
                stmt.executeUpdate(
                    "INSERT INTO rooms (room_number, room_type, price, status) VALUES " +
                    "('101', 'Single',  1500.0, 'Available')," +
                    "('102', 'Double',  2500.0, 'Available')," +
                    "('103', 'Suite',   5000.0, 'Available')," +
                    "('104', 'Single',  1500.0, 'Available')," +
                    "('105', 'Double',  2500.0, 'Available')," +
                    "('201', 'Suite',   4500.0, 'Available')," +
                    "('202', 'Single',  1800.0, 'Available')," +
                    "('203', 'Deluxe',  3500.0, 'Available')"
                );
                System.out.println("Sample rooms inserted.");
            }
            rs.close();

            System.out.println("Database initialised successfully.");

        } catch (Exception e) {
            System.err.println("DB init error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
