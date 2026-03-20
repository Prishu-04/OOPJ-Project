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
 * MyBookingsForm - Shows bookings.
 * If adminView=true → shows ALL bookings (admin).
 * If adminView=false → shows only current user's bookings.
 */
public class MyBookingsForm extends JFrame {

    private JTable             table;
    private DefaultTableModel  tableModel;

    private final User        user;
    private final boolean     adminView;
    private final BookingDAO  bookingDAO = new BookingDAO();
    private final RoomDAO     roomDAO    = new RoomDAO();

    private static final Color BG    = new Color(245, 247, 250);
    private static final Color HDR   = new Color(142, 68,  173);
    private static final Color WHITE = Color.WHITE;

    public MyBookingsForm(User user, boolean adminView) {
        this.user      = user;
        this.adminView = adminView;
        initUI();
        loadBookings();
    }

    private void initUI() {
        setTitle(adminView ? "All Bookings (Admin)" : "My Bookings");
        setSize(860, 480);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(BG);

        // Header
        JPanel header = new JPanel(new GridBagLayout());
        header.setBackground(HDR);
        header.setPreferredSize(new Dimension(860, 55));
        JLabel title = new JLabel(adminView ? "📋  All Bookings" : "📋  My Bookings — " + user.getName());
        title.setFont(new Font("SansSerif", Font.BOLD, 16));
        title.setForeground(WHITE);
        header.add(title);
        main.add(header, BorderLayout.NORTH);

        // Table
        String[] cols = {"Booking ID", "User ID", "Room No", "Type",
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
        table.setSelectionBackground(new Color(215, 189, 226));
        table.setGridColor(new Color(220, 220, 220));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Column widths
        int[] widths = {90, 60, 80, 80, 100, 100, 90, 90};
        for (int i = 0; i < widths.length; i++)
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder(10, 15, 5, 15));
        main.add(scroll, BorderLayout.CENTER);

        // Bottom bar
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 8));
        bottom.setBackground(BG);

        JButton btnRefresh = btn("🔃 Refresh", new Color(127, 140, 141));
        bottom.add(btnRefresh);

        if (!adminView) {
            JButton btnCancel = btn("❌ Cancel Booking", new Color(192, 57, 43));
            bottom.add(btnCancel);
            btnCancel.addActionListener(e -> cancelSelectedBooking());
        }

        main.add(bottom, BorderLayout.SOUTH);
        add(main);

        btnRefresh.addActionListener(e -> loadBookings());
    }

    private void loadBookings() {
        tableModel.setRowCount(0);
        List<Booking> list;

        if (adminView) {
            list = bookingDAO.getAllBookings();
        } else {
            list = bookingDAO.getBookingsByUser(user.getUserId());
        }

        for (Booking b : list) {
            Room room = roomDAO.getRoomById(b.getRoomId());
            String roomNo   = room != null ? room.getRoomNumber() : "N/A";
            String roomType = room != null ? room.getRoomType()   : "N/A";

            tableModel.addRow(new Object[]{
                b.getBookingId(),
                b.getUserId(),
                roomNo,
                roomType,
                b.getCheckIn(),
                b.getCheckOut(),
                String.format("%.2f", b.getTotalAmount()),
                b.getBookingStatus()
            });
        }
    }

    private void cancelSelectedBooking() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a booking to cancel.",
                "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String status = (String) tableModel.getValueAt(row, 7);
        if ("Cancelled".equals(status)) {
            JOptionPane.showMessageDialog(this, "This booking is already cancelled.",
                "Already Cancelled", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int bookingId = (int) tableModel.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
            "Cancel Booking ID " + bookingId + "?", "Confirm Cancellation",
            JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            Booking booking = bookingDAO.getBookingById(bookingId);
            boolean ok = bookingDAO.updateBookingStatus(bookingId, "Cancelled");
            if (ok) {
                // Free the room
                if (booking != null) {
                    roomDAO.updateRoomStatus(booking.getRoomId(), "Available");
                }
                JOptionPane.showMessageDialog(this,
                    "Booking cancelled. Room is now available.", "Cancelled",
                    JOptionPane.INFORMATION_MESSAGE);
                loadBookings();
            } else {
                JOptionPane.showMessageDialog(this, "Cancellation failed.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private JButton btn(String text, Color bg) {
        JButton b = new JButton(text);
        b.setBackground(bg);
        b.setForeground(WHITE);
        b.setFont(new Font("SansSerif", Font.BOLD, 12));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(160, 32));
        return b;
    }
}
