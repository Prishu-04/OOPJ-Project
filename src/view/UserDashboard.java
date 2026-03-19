package view;

import model.User;

import javax.swing.*;
import java.awt.*;

/**
 * UserDashboard - Main hub for logged-in customers.
 */
public class UserDashboard extends JFrame {

    private User currentUser;

    public UserDashboard(User currentUser) {
        this.currentUser = currentUser;
        setTitle("Customer Dashboard - Hotel Reservation System");
        setSize(480, 420);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        initUI();
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        mainPanel.setBackground(new Color(245, 255, 245));

        // ---- Header ----
        JLabel lblWelcome = new JLabel("Welcome, " + currentUser.getName(),
                                       SwingConstants.CENTER);
        lblWelcome.setFont(new Font("Arial", Font.BOLD, 16));
        lblWelcome.setForeground(new Color(30, 130, 60));
        mainPanel.add(lblWelcome, BorderLayout.NORTH);

        // ---- Buttons ----
        JPanel btnPanel = new JPanel(new GridLayout(6, 1, 10, 10));
        btnPanel.setBackground(new Color(245, 255, 245));

        JButton btnViewRooms    = createBtn("View Available Rooms", new Color(30, 130, 60));
        JButton btnBookRoom     = createBtn("Book a Room",          new Color(30, 130, 60));
        JButton btnMyBookings   = createBtn("My Bookings",          new Color(30, 130, 60));
        JButton btnCancelBooking= createBtn("Cancel Booking",       new Color(180, 100, 20));
        JButton btnPayment      = createBtn("Make Payment",         new Color(30, 100, 180));
        JButton btnLogout       = createBtn("Logout",               new Color(180, 40, 40));

        btnPanel.add(btnViewRooms);
        btnPanel.add(btnBookRoom);
        btnPanel.add(btnMyBookings);
        btnPanel.add(btnCancelBooking);
        btnPanel.add(btnPayment);
        btnPanel.add(btnLogout);

        mainPanel.add(btnPanel, BorderLayout.CENTER);
        add(mainPanel);

        // ---- Events ----
        btnViewRooms.addActionListener(e -> new ViewRoomsForm().setVisible(true));

        btnBookRoom.addActionListener(e -> new BookingForm(currentUser).setVisible(true));

        btnMyBookings.addActionListener(e -> new MyBookingsForm(currentUser).setVisible(true));

        btnCancelBooking.addActionListener(e -> new CancelBookingForm(currentUser).setVisible(true));

        btnPayment.addActionListener(e -> new PaymentForm(currentUser).setVisible(true));

        btnLogout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
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
        return btn;
    }
}
