package main;
import view.LoginForm;
import javax.swing.*;
/**
 * Main - Entry point of the Hotel Reservation System.
 *
 * Run this class to start the application.
 * Make sure:
 *  1. MySQL is running and the schema has been imported.
 *  2. The MySQL Connector/J JAR is on the classpath.
 *  3. DBConnection.java has the correct DB credentials.
 */
public class Main {

    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {}

        SwingUtilities.invokeLater(() -> {
            LoginForm loginForm = new LoginForm();
            loginForm.setVisible(true);
        });
    }
}
