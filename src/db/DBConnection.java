package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DBConnection - Singleton-style JDBC connection helper.
 * Provides a single point of connection to the MySQL database.
 */
public class DBConnection {

    // -------------------------------------------------------
    // Database configuration — change these as needed
    // -------------------------------------------------------
    private static final String URL      = "jdbc:mysql://localhost:3306/hotel_reservation_system";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";   // <-- set your MySQL root password here

    /**
     * Returns a new Connection object each time it is called.
     * Always close the connection in the calling code (use try-with-resources or finally block).
     */
    public static Connection getConnection() {
        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found. Add mysql-connector-j JAR to classpath.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Database connection failed. Check URL, username, and password.");
            e.printStackTrace();
        }
        return null;
    }
}
