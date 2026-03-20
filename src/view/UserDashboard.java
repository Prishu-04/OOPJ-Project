package view;

import model.User;

import javax.swing.*;
import java.awt.*;

/**
 * UserDashboard - Main screen for logged-in customers.
 */
public class UserDashboard extends JFrame {

    private final User user;

    private static final Color BG      = new Color(245, 248, 250);
    private static final Color HEADER  = new Color(41,  128, 185);
    private static final Color GRN     = new Color(39,  174, 96);
    private static final Color ORG     = new Color(230, 126, 34);
    private static final Color RED     = new Color(192, 57,  43);
    private static final Color PRP     = new Color(142, 68,  173);
    private static final Color WHITE   = Color.WHITE;

    public UserDashboard(User user) {
        this.user = user;
        initUI();
    }

    private void initUI() {
        setTitle(user.getDashboardTitle()); // Polymorphism
        setSize(640, 480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(BG);

        // ── Header ────────────────────────────────────────
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(HEADER);
        header.setPreferredSize(new Dimension(640, 70));
        header.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        JLabel title = new JLabel("🏨  Hotel Reservation System");
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        title.setForeground(WHITE);
        header.add(title, BorderLayout.WEST);

        JLabel welcome = new JLabel("Hello, " + user.getName() + "  ");
        welcome.setFont(new Font("SansSerif", Font.ITALIC, 13));
        welcome.setForeground(new Color(210, 235, 255));
        header.add(welcome, BorderLayout.EAST);

        main.add(header, BorderLayout.NORTH);

        // ── Centre panel ──────────────────────────────────
        JPanel centre = new JPanel(new BorderLayout());
        centre.setBackground(BG);

        JLabel subTitle = new JLabel("What would you like to do?", SwingConstants.CENTER);
        subTitle.setFont(new Font("SansSerif", Font.BOLD, 15));
        subTitle.setForeground(new Color(44, 62, 80));
        subTitle.setBorder(BorderFactory.createEmptyBorder(18, 0, 8, 0));
        centre.add(subTitle, BorderLayout.NORTH);

        JPanel btnGrid = new JPanel(new GridLayout(2, 3, 15, 15));
        btnGrid.setBackground(BG);
        btnGrid.setBorder(BorderFactory.createEmptyBorder(10, 40, 30, 40));

        btnGrid.add(dBtn("🛏️  View Rooms",       HEADER, e -> new ViewRoomsForm(false).setVisible(true)));
        btnGrid.add(dBtn("🔍  Search Rooms",      HEADER, e -> new ViewRoomsForm(false).setVisible(true)));
        btnGrid.add(dBtn("📅  Book a Room",       GRN,    e -> new BookingForm(user).setVisible(true)));
        btnGrid.add(dBtn("📋  My Bookings",       PRP,    e -> new MyBookingsForm(user, false).setVisible(true)));
        btnGrid.add(dBtn("❌  Cancel Booking",    ORG,    e -> new CancelBookingForm(user).setVisible(true)));
        btnGrid.add(dBtn("🚪  Logout",            RED,    e -> logout()));

        centre.add(btnGrid, BorderLayout.CENTER);
        main.add(centre, BorderLayout.CENTER);
        add(main);
    }

    private void logout() {
        int c = JOptionPane.showConfirmDialog(this,
            "Logout from the system?", "Confirm Logout",
            JOptionPane.YES_NO_OPTION);
        if (c == JOptionPane.YES_OPTION) {
            new LoginForm().setVisible(true);
            dispose();
        }
    }

    private JButton dBtn(String text, Color bg,
                         java.awt.event.ActionListener al) {
        JButton btn = new JButton("<html><center>" + text + "</center></html>");
        btn.setBackground(bg);
        btn.setForeground(WHITE);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(160, 70));
        btn.addActionListener(al);
        return btn;
    }
}
