package view;

import dao.BookingDAO;
import model.Booking;
import model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * MyBookingsForm - Shows the logged-in user's bookings in a JTable.
 */
public class MyBookingsForm extends JFrame {

    private JTable            table;
    private DefaultTableModel tableModel;
    private JButton           btnRefresh, btnClose;

    private User       currentUser;
    private BookingDAO bookingDAO = new BookingDAO();

    public MyBookingsForm(User currentUser) {
        this.currentUser = currentUser;
        setTitle("My Bookings");
        setSize(750, 420);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        initUI();
        loadMyBookings();
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 10, 15));

        JLabel lblTitle = new JLabel("My Bookings - " + currentUser.getName(),
                                     SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 15));
        lblTitle.setForeground(new Color(30, 100, 60));
        mainPanel.add(lblTitle, BorderLayout.NORTH);

        // ---- Table ----
        String[] cols = {"Booking ID", "Room No", "Type", "Check-In", "Check-Out",
                         "Total (₹)", "Status"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        table.setRowHeight(24);

        mainPanel.add(new JScrollPane(table), BorderLayout.CENTER);

        // ---- Buttons ----
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 5));
        btnRefresh = new JButton("Refresh");
        btnClose   = new JButton("Close");
        btnRefresh.setBackground(new Color(30, 100, 180));
        btnRefresh.setForeground(Color.WHITE);
        btnRefresh.setFocusPainted(false);
        btnPanel.add(btnRefresh);
        btnPanel.add(btnClose);
        mainPanel.add(btnPanel, BorderLayout.SOUTH);

        add(mainPanel);

        btnRefresh.addActionListener(e -> loadMyBookings());
        btnClose.addActionListener(e -> dispose());
    }

    private void loadMyBookings() {
        tableModel.setRowCount(0);
        List<Booking> bookings = bookingDAO.getBookingsByUser(currentUser.getUserId());
        for (Booking b : bookings) {
            tableModel.addRow(new Object[]{
                b.getBookingId(),
                b.getRoomNumber(),
                b.getRoomType(),
                b.getCheckIn(),
                b.getCheckOut(),
                String.format("%.2f", b.getTotalAmount()),
                b.getBookingStatus()
            });
        }
        if (bookings.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "You have no bookings yet.", "No Bookings",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
