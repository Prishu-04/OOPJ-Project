package view;

import dao.BookingDAO;
import dao.RoomDAO;
import model.Booking;
import model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * CancelBookingForm - Customer can cancel their own active bookings.
 * On cancellation, the booking status changes to Cancelled
 * and the room status reverts to Available.
 */
public class CancelBookingForm extends JFrame {

    private JTable            table;
    private DefaultTableModel tableModel;
    private JButton           btnCancel, btnRefresh, btnClose;

    private User       currentUser;
    private BookingDAO bookingDAO = new BookingDAO();
    private RoomDAO    roomDAO    = new RoomDAO();

    public CancelBookingForm(User currentUser) {
        this.currentUser = currentUser;
        setTitle("Cancel Booking");
        setSize(750, 420);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        initUI();
        loadBookings();
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 10, 15));

        JLabel lbl = new JLabel("Select a Booking to Cancel", SwingConstants.CENTER);
        lbl.setFont(new Font("Arial", Font.BOLD, 15));
        lbl.setForeground(new Color(160, 60, 20));
        mainPanel.add(lbl, BorderLayout.NORTH);

        // ---- Table ----
        String[] cols = {"Booking ID", "Room No", "Type", "Check-In", "Check-Out",
                         "Total (₹)", "Status", "Room ID"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        table.setRowHeight(24);
        // Hide the Room ID column (used internally)
        table.getColumnModel().getColumn(7).setMinWidth(0);
        table.getColumnModel().getColumn(7).setMaxWidth(0);
        table.getColumnModel().getColumn(7).setWidth(0);

        mainPanel.add(new JScrollPane(table), BorderLayout.CENTER);

        // ---- Buttons ----
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 8));

        btnCancel  = new JButton("Cancel Selected Booking");
        btnRefresh = new JButton("Refresh");
        btnClose   = new JButton("Close");

        styleBtn(btnCancel,  new Color(180, 40, 40));
        styleBtn(btnRefresh, new Color(80, 130, 80));

        btnPanel.add(btnCancel);
        btnPanel.add(btnRefresh);
        btnPanel.add(btnClose);
        mainPanel.add(btnPanel, BorderLayout.SOUTH);

        add(mainPanel);

        btnRefresh.addActionListener(e -> loadBookings());
        btnClose.addActionListener(e -> dispose());
        btnCancel.addActionListener(e -> cancelSelected());
    }

    private void loadBookings() {
        tableModel.setRowCount(0);
        List<Booking> bookings = bookingDAO.getBookingsByUser(currentUser.getUserId());
        for (Booking b : bookings) {
            // Only show Confirmed bookings (already cancelled ones can't be cancelled again)
            tableModel.addRow(new Object[]{
                b.getBookingId(),
                b.getRoomNumber(),
                b.getRoomType(),
                b.getCheckIn(),
                b.getCheckOut(),
                String.format("%.2f", b.getTotalAmount()),
                b.getBookingStatus(),
                b.getRoomId()   // hidden
            });
        }
    }

    private void cancelSelected() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a booking to cancel.");
            return;
        }

        String status    = (String) tableModel.getValueAt(row, 6);
        if ("Cancelled".equalsIgnoreCase(status)) {
            JOptionPane.showMessageDialog(this,
                "This booking is already cancelled.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int bookingId = (int) tableModel.getValueAt(row, 0);
        int roomId    = (int) tableModel.getValueAt(row, 7);
        String roomNo = (String) tableModel.getValueAt(row, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
            "Cancel booking for Room " + roomNo + "?\n"
            + "The room will be made available again.",
            "Confirm Cancellation", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm != JOptionPane.YES_OPTION) return;

        // Update booking status
        boolean cancelled = bookingDAO.cancelBooking(bookingId);

        if (cancelled) {
            // Free up the room
            roomDAO.updateStatus(roomId, "Available");
            JOptionPane.showMessageDialog(this,
                "Booking cancelled successfully. Room is now available.",
                "Cancelled", JOptionPane.INFORMATION_MESSAGE);
            loadBookings();
        } else {
            JOptionPane.showMessageDialog(this,
                "Cancellation failed. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void styleBtn(JButton btn, Color bg) {
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
    }
}
