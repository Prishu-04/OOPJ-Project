package view;

import dao.BookingDAO;
import dao.RoomDAO;
import model.Room;
import model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * BookingForm - Allows a customer to select an available room and book it.
 * Calculates total: days × price. Updates room status to Booked.
 */
public class BookingForm extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField tfCheckIn;
    private JTextField tfCheckOut;
    private JLabel     lblTotal;

    private final User       user;
    private final RoomDAO    roomDAO    = new RoomDAO();
    private final BookingDAO bookingDAO = new BookingDAO();

    private static final Color BG    = new Color(245, 247, 250);
    private static final Color HDR   = new Color(39,  174, 96);
    private static final Color WHITE = Color.WHITE;
    private static final Color DARK  = new Color(44,  62,  80);

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public BookingForm(User user) {
        this.user = user;
        initUI();
        loadAvailableRooms();
    }

    private void initUI() {
        setTitle("Book a Room");
        setSize(720, 540);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(BG);

        // Header
        JPanel header = new JPanel(new GridBagLayout());
        header.setBackground(HDR);
        header.setPreferredSize(new Dimension(720, 55));
        JLabel title = new JLabel("📅  Book a Room — Logged in: " + user.getName());
        title.setFont(new Font("SansSerif", Font.BOLD, 15));
        title.setForeground(WHITE);
        header.add(title);
        main.add(header, BorderLayout.NORTH);

        // ── Table ─────────────────────────────────────────
        String[] cols = {"Room ID", "Room No", "Type", "Price/Night (₹)", "Status"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));
        table.setRowHeight(26);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        table.getTableHeader().setBackground(HDR);
        table.getTableHeader().setForeground(WHITE);
        table.setSelectionBackground(new Color(169, 223, 191));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setPreferredSize(new Dimension(700, 230));
        scroll.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(HDR),
            "Available Rooms (click to select)"
        ));

        // ── Booking form ──────────────────────────────────
        JPanel bookPanel = new JPanel(new GridBagLayout());
        bookPanel.setBackground(BG);
        bookPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199)),
            "Enter Booking Dates"
        ));
        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.insets = new Insets(8, 10, 8, 10);

        // Check-in
        g.gridx=0; g.gridy=0;
        bookPanel.add(lbl("Check-in Date (yyyy-MM-dd):"), g);
        g.gridx=1; tfCheckIn = tf("yyyy-MM-dd"); bookPanel.add(tfCheckIn, g);

        // Check-out
        g.gridx=0; g.gridy=1;
        bookPanel.add(lbl("Check-out Date (yyyy-MM-dd):"), g);
        g.gridx=1; tfCheckOut = tf("yyyy-MM-dd"); bookPanel.add(tfCheckOut, g);

        // Calculate button
        g.gridx=2; g.gridy=0;
        JButton btnCalc = new JButton("Calculate");
        btnCalc.setBackground(new Color(41, 128, 185));
        btnCalc.setForeground(WHITE);
        btnCalc.setFocusPainted(false);
        btnCalc.setBorderPainted(false);
        btnCalc.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        bookPanel.add(btnCalc, g);

        // Total label
        g.gridx=0; g.gridy=2;
        bookPanel.add(lbl("Estimated Total:"), g);
        g.gridx=1;
        lblTotal = new JLabel("—");
        lblTotal.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblTotal.setForeground(new Color(192, 57, 43));
        bookPanel.add(lblTotal, g);

        // Book button
        g.gridx=0; g.gridy=3; g.gridwidth=3;
        JButton btnBook = new JButton("✅  Confirm Booking");
        btnBook.setBackground(HDR);
        btnBook.setForeground(WHITE);
        btnBook.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnBook.setFocusPainted(false);
        btnBook.setBorderPainted(false);
        btnBook.setPreferredSize(new Dimension(250, 38));
        btnBook.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        bookPanel.add(btnBook, g);

        // ── Centre layout ─────────────────────────────────
        JPanel centre = new JPanel(new BorderLayout(0, 10));
        centre.setBackground(BG);
        centre.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        centre.add(scroll, BorderLayout.CENTER);
        centre.add(bookPanel, BorderLayout.SOUTH);
        main.add(centre, BorderLayout.CENTER);

        add(main);

        btnCalc.addActionListener(e -> calculateTotal());
        btnBook.addActionListener(e -> handleBooking());
    }

    private void loadAvailableRooms() {
        tableModel.setRowCount(0);
        List<Room> rooms = roomDAO.getAvailableRooms();
        for (Room r : rooms) {
            tableModel.addRow(new Object[]{
                r.getRoomId(), r.getRoomNumber(), r.getRoomType(),
                String.format("%.2f", r.getPrice()), r.getStatus()
            });
        }
    }

    private void calculateTotal() {
        int row = table.getSelectedRow();
        if (row < 0) {
            lblTotal.setText("Select a room first");
            return;
        }
        try {
            LocalDate ci  = LocalDate.parse(tfCheckIn.getText().trim(), FMT);
            LocalDate co  = LocalDate.parse(tfCheckOut.getText().trim(), FMT);
            if (!co.isAfter(ci)) {
                lblTotal.setText("Invalid dates");
                return;
            }
            long days  = ChronoUnit.DAYS.between(ci, co);
            double pn  = Double.parseDouble(tableModel.getValueAt(row, 3).toString());
            lblTotal.setText("₹ " + String.format("%.2f", days * pn) +
                             "  (" + days + " night" + (days > 1 ? "s" : "") + ")");
        } catch (DateTimeParseException ex) {
            lblTotal.setText("Invalid date format");
        }
    }

    private void handleBooking() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a room.", "No Room",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        String ciStr = tfCheckIn.getText().trim();
        String coStr = tfCheckOut.getText().trim();

        if (ciStr.isEmpty() || coStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter check-in and check-out dates.",
                "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        LocalDate ci, co;
        try {
            ci = LocalDate.parse(ciStr, FMT);
            co = LocalDate.parse(coStr, FMT);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Use format: yyyy-MM-dd (e.g. 2025-06-15)",
                "Date Format Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!ci.isAfter(LocalDate.now().minusDays(1))) {
            JOptionPane.showMessageDialog(this,
                "Check-in date cannot be in the past.", "Invalid Date",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!co.isAfter(ci)) {
            JOptionPane.showMessageDialog(this,
                "Check-out must be after check-in.", "Invalid Date",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        int    roomId      = (int)    tableModel.getValueAt(row, 0);
        String roomNumber  = (String) tableModel.getValueAt(row, 1);
        double pricePerNight = Double.parseDouble(tableModel.getValueAt(row, 3).toString());
        long   days        = ChronoUnit.DAYS.between(ci, co);
        double total       = days * pricePerNight;

        int confirm = JOptionPane.showConfirmDialog(this,
            "Room: " + roomNumber + "\nCheck-in:  " + ci + "\nCheck-out: " + co +
            "\nDays: " + days + "\nTotal: ₹" + String.format("%.2f", total) +
            "\n\nProceed to booking?",
            "Confirm Booking", JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) return;

        int bookingId = bookingDAO.addBooking(user.getUserId(), roomId,
                                              ciStr, coStr, total);
        if (bookingId > 0) {
            roomDAO.updateRoomStatus(roomId, "Booked");
            JOptionPane.showMessageDialog(this,
                "Booking confirmed!\nBooking ID: " + bookingId +
                "\nTotal Amount: ₹" + String.format("%.2f", total),
                "Booking Successful", JOptionPane.INFORMATION_MESSAGE);
            // Open payment form
            new PaymentForm(bookingId, total).setVisible(true);
            loadAvailableRooms();
        } else {
            JOptionPane.showMessageDialog(this, "Booking failed. Try again.", "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private JLabel lbl(String t) {
        JLabel l = new JLabel(t);
        l.setFont(new Font("SansSerif", Font.BOLD, 13));
        l.setForeground(DARK);
        return l;
    }

    private JTextField tf(String hint) {
        JTextField t = new JTextField();
        t.setToolTipText(hint);
        t.setPreferredSize(new Dimension(160, 30));
        t.setFont(new Font("SansSerif", Font.PLAIN, 13));
        t.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199)),
            BorderFactory.createEmptyBorder(3, 7, 3, 7)
        ));
        return t;
    }
}
