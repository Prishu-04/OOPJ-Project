package view;

import model.User;

import javax.swing.*;
import java.awt.*;

/**
 * AdminDashboard - Main hub for the Admin.
 * Buttons open sub-forms for room and booking management.
 */
public class AdminDashboard extends JFrame {

    private User adminUser;

    public AdminDashboard(User adminUser) {
        this.adminUser = adminUser;
        setTitle("Admin Dashboard - Hotel Reservation System");
        setSize(480, 430);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        initUI();
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        mainPanel.setBackground(new Color(240, 248, 255));

        // ---- Header ----
        JLabel lblWelcome = new JLabel("Welcome, " + adminUser.getName() + " (Admin)",
                                       SwingConstants.CENTER);
        lblWelcome.setFont(new Font("Arial", Font.BOLD, 16));
        lblWelcome.setForeground(new Color(30, 80, 150));
        mainPanel.add(lblWelcome, BorderLayout.NORTH);

        // ---- Menu Buttons ----
        JPanel btnPanel = new JPanel(new GridLayout(6, 1, 10, 10));
        btnPanel.setBackground(new Color(240, 248, 255));

        JButton btnAddRoom      = createBtn("Add New Room",         new Color(30, 100, 180));
        JButton btnManageRooms  = createBtn("Manage Rooms",         new Color(30, 100, 180));
        JButton btnViewBookings = createBtn("View All Bookings",    new Color(30, 100, 180));
        JButton btnViewCustomers= createBtn("View All Customers",   new Color(30, 100, 180));
        JButton btnLogout       = createBtn("Logout",               new Color(180, 40, 40));

        btnPanel.add(btnAddRoom);
        btnPanel.add(btnManageRooms);
        btnPanel.add(btnViewBookings);
        btnPanel.add(btnViewCustomers);
        btnPanel.add(btnLogout);

        mainPanel.add(btnPanel, BorderLayout.CENTER);
        add(mainPanel);

        // ---- Events ----
        btnAddRoom.addActionListener(e -> {
            new AddRoomForm().setVisible(true);
        });

        btnManageRooms.addActionListener(e -> {
            new ManageRoomsForm().setVisible(true);
        });

        btnViewBookings.addActionListener(e -> {
            new ViewAllBookingsForm().setVisible(true);
        });

        btnViewCustomers.addActionListener(e -> {
            new ViewCustomersForm().setVisible(true);
        });

        btnLogout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to logout?", "Logout",
                JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                new LoginForm().setVisible(true);
                dispose();
            }
        });
    }

    private JButton createBtn(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(300, 45));
        return btn;
    }
}
