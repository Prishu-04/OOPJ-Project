package view;

import dao.BookingDAO;
import dao.RoomDAO;
import model.Booking;
import model.Room;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * BookingForm - Customer selects a room, enters check-in/check-out dates,
 * and confirms the booking. Total amount is calculated automatically.
 */
public class BookingForm extends JFrame {

    private JComboBox<String> cbRoom;
    private JTextField        tfCheckIn, tfCheckOut, tfTotal;
    private JButton           btnCalculate, btnBook, btnClose;

    private User       currentUser;
    private RoomDAO    roomDAO    = new RoomDAO();
    private BookingDAO bookingDAO = new BookingDAO();

    // Store available rooms for reference
    private List<Room> availableRooms;

    public BookingForm(User currentUser) {
        this.currentUser = currentUser;
        setTitle("Book a Room");
        setSize(420, 380);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        initUI();
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 15, 25));
        mainPanel.setBackground(Color.WHITE);

        JLabel lblTitle = new JLabel("Book a Room", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitle.setForeground(new Color(30, 80, 150));
        mainPanel.add(lblTitle, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets  = new Insets(8, 5, 8, 5);
        gbc.fill    = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        // Room selector
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Select Room:"), gbc);
        gbc.gridx = 1;
        cbRoom = new JComboBox<>();
        loadAvailableRooms();
        formPanel.add(cbRoom, gbc);

        // Check-in
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Check-In (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        tfCheckIn = new JTextField(15);
        formPanel.add(tfCheckIn, gbc);

        // Check-out
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Check-Out (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        tfCheckOut = new JTextField(15);
        formPanel.add(tfCheckOut, gbc);

        // Total amount (read-only)
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Total Amount (₹):"), gbc);
        gbc.gridx = 1;
        tfTotal = new JTextField(15);
        tfTotal.setEditable(false);
        tfTotal.setBackground(new Color(230, 230, 230));
        formPanel.add(tfTotal, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // ---- Buttons ----
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 8));
        btnPanel.setBackground(Color.WHITE);

        btnCalculate = new JButton("Calculate Total");
        btnBook      = new JButton("Confirm Booking");
        btnClose     = new JButton("Cancel");

        styleBtn(btnCalculate, new Color(80, 130, 80));
        styleBtn(btnBook,      new Color(30, 100, 180));

        btnPanel.add(btnCalculate);
        btnPanel.add(btnBook);
        btnPanel.add(btnClose);
        mainPanel.add(btnPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // ---- Events ----
        btnCalculate.addActionListener(e -> calculateTotal());
        btnBook.addActionListener(e -> confirmBooking());
        btnClose.addActionListener(e -> dispose());
    }

    /** Load available rooms into the combo box */
    private void loadAvailableRooms() {
        availableRooms = roomDAO.getAvailableRooms();
        cbRoom.removeAllItems();
        if (availableRooms.isEmpty()) {
            cbRoom.addItem("No rooms available");
        } else {
            for (Room r : availableRooms) {
                cbRoom.addItem("Room " + r.getRoomNumber()
                        + " | " + r.getRoomType()
                        + " | ₹" + r.getPrice() + "/night");
            }
        }
    }

    /** Parse dates and calculate total amount */
    private void calculateTotal() {
        if (availableRooms.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No available rooms to book.");
            return;
        }

        Date checkIn  = parseDate(tfCheckIn.getText().trim());
        Date checkOut = parseDate(tfCheckOut.getText().trim());

        if (checkIn == null || checkOut == null) {
            JOptionPane.showMessageDialog(this,
                "Please enter dates in YYYY-MM-DD format.", "Date Error",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!checkOut.after(checkIn)) {
            JOptionPane.showMessageDialog(this,
                "Check-Out date must be after Check-In date.", "Date Error",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (checkIn.before(new Date(System.currentTimeMillis() - 86400000L))) {
            JOptionPane.showMessageDialog(this,
                "Check-In date cannot be in the past.", "Date Error",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        long diff  = checkOut.getTime() - checkIn.getTime();
        long days  = TimeUnit.MILLISECONDS.toDays(diff);

        int   idx   = cbRoom.getSelectedIndex();
        Room  room  = availableRooms.get(idx);
        double total = days * room.getPrice();

        tfTotal.setText(String.format("%.2f", total));
    }

    /** Validate inputs and save booking to database */
    private void confirmBooking() {
        if (availableRooms.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No available rooms.");
            return;
        }
        if (tfTotal.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please click 'Calculate Total' first.", "Validation Error",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        Date checkIn  = parseDate(tfCheckIn.getText().trim());
        Date checkOut = parseDate(tfCheckOut.getText().trim());
        if (checkIn == null || checkOut == null || !checkOut.after(checkIn)) {
            JOptionPane.showMessageDialog(this,
                "Please fix the date errors first.", "Date Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int   idx    = cbRoom.getSelectedIndex();
        Room  room   = availableRooms.get(idx);
        double total = Double.parseDouble(tfTotal.getText());

        // Confirm dialog
        int choice = JOptionPane.showConfirmDialog(this,
            "Confirm booking:\n"
            + "Room: " + room.getRoomNumber() + " (" + room.getRoomType() + ")\n"
            + "Check-In:  " + checkIn + "\n"
            + "Check-Out: " + checkOut + "\n"
            + "Total:     ₹" + String.format("%.2f", total),
            "Confirm Booking", JOptionPane.YES_NO_OPTION);

        if (choice != JOptionPane.YES_OPTION) return;

        // Save booking
        Booking booking = new Booking(
            currentUser.getUserId(), room.getRoomId(),
            checkIn, checkOut, total, "Confirmed"
        );

        int newBookingId = bookingDAO.addAndGetId(booking);
        if (newBookingId > 0) {
            // Mark room as Booked
            roomDAO.updateStatus(room.getRoomId(), "Booked");

            // Show receipt
            showReceipt(newBookingId, room, checkIn, checkOut, total);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                "Booking failed. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /** Show booking confirmation receipt in a dialog */
    private void showReceipt(int bookingId, Room room, Date checkIn, Date checkOut, double total) {
        String receipt =
            "========== BOOKING CONFIRMATION ==========\n"
            + "Booking ID  : " + bookingId + "\n"
            + "Customer    : " + currentUser.getName() + "\n"
            + "Room No     : " + room.getRoomNumber() + "\n"
            + "Room Type   : " + room.getRoomType() + "\n"
            + "Check-In    : " + checkIn + "\n"
            + "Check-Out   : " + checkOut + "\n"
            + "Total Amount: ₹" + String.format("%.2f", total) + "\n"
            + "Status      : Confirmed\n"
            + "==========================================\n"
            + "Thank you for choosing our hotel!";

        JTextArea ta = new JTextArea(receipt);
        ta.setFont(new Font("Monospaced", Font.PLAIN, 13));
        ta.setEditable(false);

        JOptionPane.showMessageDialog(this, ta, "Booking Receipt",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /** Parse a date string in YYYY-MM-DD format */
    private Date parseDate(String text) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false);
            java.util.Date parsed = sdf.parse(text);
            return new Date(parsed.getTime());
        } catch (Exception e) {
            return null;
        }
    }

    private void styleBtn(JButton btn, Color bg) {
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
    }
}
