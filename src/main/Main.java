package main;

import db.DBConnection;
import view.LoginForm;

import javax.swing.*;

/**
 * Main - Application entry point.
 * Initialises the database and launches the LoginForm.
 */
public class Main {

    public static void main(String[] args) {

        // ── 1. Set a native look & feel ───────────────────
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        // ── 2. Initialise SQLite database on first run ────
        DBConnection.initializeDatabase();

        // ── 3. Launch GUI on the Event Dispatch Thread ────
        SwingUtilities.invokeLater(() -> {
            LoginForm login = new LoginForm();
            login.setVisible(true);
        });
    }
}
