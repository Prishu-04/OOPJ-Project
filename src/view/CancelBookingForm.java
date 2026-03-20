package view;

import dao.BookingDAO;
import dao.RoomDAO;
import model.Booking;
import model.Room;
import model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * CancelBookingForm - Shows user's active bookings and allows cancellation.
 */
public class CancelBookingForm extends JFrame {

    private JTable            table;
    private DefaultTableModel tableModel;

    private final User       user;
    private final BookingDAO bookingDAO = new BookingDAO();
    private final RoomDAO    roomDAO    = new RoomDAO();

    private static final Color BG    = new Color(245, 247, 250);
    private static final Color HDR   = new Color(211, 84,  0);
    private static final Color WHITE = Color.WHITE;
    private static final Color RED   = new Color(192, 57, 43);

    public CancelBookingForm(User user) {
        this.user = user;
        initUI();
        loadActiveBookings();
    }

    private void initUI() {
        setTitle("Cancel Booking");
        setSize(800, 430);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(BG);

        // Header
        JPanel header = new JPanel(new GridBagLayout());
        header.setBackground(HDR);
        header.setPreferredSize(new Dimension(800, 55));
        JLabel title = new JLabel("❌  Cancel a Booking — " + user.getName());
        title.setFont(new Font("SansSerif", Font.BOLD, 16));
        title.setForeground(WHITE);
        header.add(title);
        main.add(header, BorderLayout.NORTH);

        // Info label
        JLabel info = new JLabel(
            "  Your active (Confirmed) bookings are listed below. Select one and click Cancel.",
            SwingConstants.LEFT
        );
        info.setFont(new Font("SansSerif", Font.ITALIC, 12));
        info.setForeground(new Color(100, 100, 100));
        info.setBorder(BorderFactory.createEmptyBorder(8, 15, 4, 0));
        main.add(info, BorderLayout.BEFORE_FIRST_LINE);

        // Table
        String[] cols = {"Booking ID", "Room No", "Room Type",
                         "Check-in", "Check-out", "Total (₹)", "Status"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));
        table.setRowHeight(26);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        table.getTableHeader().setBackground(HDR);
        table.getTableHeader().setForeground(WHITE);
        table.setSelectionBackground(new Color(250, 215, 160));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setGridColor(new Color(220, 220, 220));

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder(8, 15, 5, 15));

        JPanel centre = new JPanel(new BorderLayout());
        centre.setBackground(BG);
        centre.add(info, BorderLayout.NORTH);
        centre.add(scroll, BorderLayout.CENTER);
        main.add(centre, BorderLayout.CENTER);

        // Buttons
        JPanel btnBar = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnBar.setBackground(BG);

        JButton btnCancel  = btn("❌  Cancel Selected Booking", RED);
        JButton btnRefresh = btn("🔃 Refresh", new Color(127, 140, 141));
        btnBar.add(btnCancel);
        btnBar.add(btnRefresh);
        main.add(btnBar, BorderLayout.SOUTH);

        add(main);

        btnCancel.addActionListener(e  -> handleCancel());
        btnRefresh.addActionListener(e -> loadActiveBookings());
    }

    private void loadActiveBookings() {
        tableModel.setRowCount(0);
        List<Booking> bookings = bookingDAO.getActiveBookingsByUser(user.getUserId());

        if (bookings.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "You have no active bookings to cancel.", "No Bookings",
                JOptionPane.INFORMATION_MESSAGE);
        }

        for (Booking b : bookings) {
            Room room = roomDAO.getRoomById(b.getRoomId());
            String roomNo   = room != null ? room.getRoomNumber() : "N/A";
            String roomType = room != null ? room.getRoomType()   : "N/A";
            tableModel.addRow(new Object[]{
                b.getBookingId(), roomNo, roomType,
                b.getCheckIn(), b.getCheckOut(),
                String.format("%.2f", b.getTotalAmount()),
                b.getBookingStatus()
            });
        }
    }

    private void handleCancel() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this,
                "Please select a booking to cancel.", "No Selection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        int    bookingId = (int)    tableModel.getValueAt(row, 0);
        String roomNo    = (String) tableModel.getValueAt(row, 1);
        String checkIn   = (String) tableModel.getValueAt(row, 3);
        String checkOut  = (String) tableModel.getValueAt(row, 4);

        int confirm = JOptionPane.showConfirmDialog(this,
            "Cancel this booking?\n\n" +
            "Room: " + roomNo + "\n" +
            "Check-in:  " + checkIn + "\n" +
            "Check-out: " + checkOut + "\n\n" +
            "This action cannot be undone.",
            "Confirm Cancellation", JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);

        if (confirm != JOptionPane.YES_OPTION) return;

        Booking booking = bookingDAO.getBookingById(bookingId);
        boolean ok = bookingDAO.updateBookingStatus(bookingId, "Cancelled");

        if (ok) {
            // Mark room as Available again
            if (booking != null) {
                roomDAO.updateRoomStatus(booking.getRoomId(), "Available");
            }
            JOptionPane.showMessageDialog(this,
                "Booking #" + bookingId + " has been cancelled.\n" +
                "Room " + roomNo + " is now available.",
                "Cancellation Successful", JOptionPane.INFORMATION_MESSAGE);
            loadActiveBookings();
        } else {
            JOptionPane.showMessageDialog(this,
                "Failed to cancel booking. Please try again.", "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private JButton btn(String text, Color bg) {
        JButton b = new JButton(text);
        b.setBackground(bg);
        b.setForeground(WHITE);
        b.setFont(new Font("SansSerif", Font.BOLD, 13));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setPreferredSize(new Dimension(220, 34));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }
}
