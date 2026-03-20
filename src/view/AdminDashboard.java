package view;

import model.User;

import javax.swing.*;
import java.awt.*;

/**
 * AdminDashboard - Main control panel for the admin.
 */
public class AdminDashboard extends JFrame {

    private final User admin;

    private static final Color BG       = new Color(236, 240, 241);
    private static final Color HEADER   = new Color(44,  62,  80);
    private static final Color BTN_BLU  = new Color(41,  128, 185);
    private static final Color BTN_GRN  = new Color(39,  174, 96);
    private static final Color BTN_ORG  = new Color(211, 84,  0);
    private static final Color BTN_PRP  = new Color(142, 68,  173);
    private static final Color BTN_RED  = new Color(192, 57,  43);
    private static final Color WHITE    = Color.WHITE;

    public AdminDashboard(User admin) {
        this.admin = admin;
        initUI();
    }

    private void initUI() {
        setTitle(admin.getDashboardTitle()); // Polymorphism
        setSize(700, 530);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(BG);

        // ── Header ────────────────────────────────────────
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(HEADER);
        header.setPreferredSize(new Dimension(700, 75));
        header.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        JLabel title = new JLabel("🏨  Hotel Management System");
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        title.setForeground(WHITE);
        header.add(title, BorderLayout.WEST);

        JLabel welcome = new JLabel("Welcome, " + admin.getName() + "  ");
        welcome.setFont(new Font("SansSerif", Font.ITALIC, 13));
        welcome.setForeground(new Color(189, 195, 199));
        header.add(welcome, BorderLayout.EAST);

        main.add(header, BorderLayout.NORTH);

        // ── Section label ─────────────────────────────────
        JLabel sectionLbl = new JLabel("Admin Control Panel", SwingConstants.CENTER);
        sectionLbl.setFont(new Font("SansSerif", Font.BOLD, 15));
        sectionLbl.setForeground(HEADER);
        sectionLbl.setBorder(BorderFactory.createEmptyBorder(18, 0, 4, 0));
        main.add(sectionLbl, BorderLayout.CENTER);

        // ── Buttons grid ──────────────────────────────────
        JPanel grid = new JPanel(new GridLayout(3, 3, 15, 15));
        grid.setBackground(BG);
        grid.setBorder(BorderFactory.createEmptyBorder(10, 30, 20, 30));

        // Row 1 — Room management
        grid.add(dashBtn("➕  Add Room",          BTN_GRN,  e -> new AddRoomForm().setVisible(true)));
        grid.add(dashBtn("✏️  Manage Rooms",       BTN_BLU,  e -> new ManageRoomsForm().setVisible(true)));
        grid.add(dashBtn("🛏️  View All Rooms",     BTN_BLU,  e -> new ViewRoomsForm(true).setVisible(true)));

        // Row 2 — Booking & customers
        grid.add(dashBtn("📋  All Bookings",       BTN_PRP,  e -> openAllBookings()));
        grid.add(dashBtn("👥  View Customers",      BTN_PRP,  e -> new ViewCustomersForm().setVisible(true)));
        grid.add(dashBtn("💳  View Payments",       BTN_ORG,  e -> openPayments()));

        // Row 3 — Misc
        grid.add(dashBtn("🔑  Change Room Status",  BTN_ORG,  e -> new ManageRoomsForm().setVisible(true)));
        grid.add(new JLabel()); // spacer
        grid.add(dashBtn("🚪  Logout",              BTN_RED,  e -> logout()));

        main.add(grid, BorderLayout.SOUTH);
        // Re-layout
        main.remove(sectionLbl);

        JPanel center = new JPanel(new BorderLayout());
        center.setBackground(BG);
        center.add(sectionLbl, BorderLayout.NORTH);
        center.add(grid, BorderLayout.CENTER);
        main.add(center, BorderLayout.CENTER);

        add(main);
    }

    private void openAllBookings() {
        // Show all bookings in a simple JTable dialog
        new MyBookingsForm(admin, true).setVisible(true);
    }

    private void openPayments() {
        JOptionPane.showMessageDialog(this,
            "Payment records feature: open the PaymentForm from a booking.",
            "Payments", JOptionPane.INFORMATION_MESSAGE);
    }

    private void logout() {
        int choice = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to logout?", "Logout",
            JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            new LoginForm().setVisible(true);
            dispose();
        }
    }

    // ── Helper: create a styled dashboard button ──────────
    private JButton dashBtn(String text, Color bg,
                            java.awt.event.ActionListener al) {
        JButton btn = new JButton("<html><center>" + text + "</center></html>");
        btn.setBackground(bg);
        btn.setForeground(WHITE);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(180, 65));
        btn.addActionListener(al);
        return btn;
    }
}
