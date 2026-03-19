package view;

import dao.BookingDAO;
import dao.PaymentDAO;
import model.Booking;
import model.Payment;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.sql.Date;
import java.util.List;

/**
 * PaymentForm - Customer records a payment against one of their Confirmed bookings.
 */
public class PaymentForm extends JFrame {

    private JComboBox<String> cbBooking;
    private JTextField        tfAmount;
    private JComboBox<String> cbMethod;
    private JButton           btnPay, btnClose;

    private User       currentUser;
    private BookingDAO bookingDAO  = new BookingDAO();
    private PaymentDAO paymentDAO  = new PaymentDAO();

    // Store fetched bookings for cross-reference
    private List<Booking> confirmedBookings;

    public PaymentForm(User currentUser) {
        this.currentUser = currentUser;
        setTitle("Make Payment");
        setSize(420, 330);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        initUI();
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 15, 25));
        mainPanel.setBackground(Color.WHITE);

        JLabel lblTitle = new JLabel("Payment", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitle.setForeground(new Color(30, 80, 150));
        mainPanel.add(lblTitle, BorderLayout.NORTH);

        // ---- Form ----
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets  = new Insets(9, 5, 9, 5);
        gbc.fill    = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        // Select Booking
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Select Booking:"), gbc);
        gbc.gridx = 1;
        cbBooking = new JComboBox<>();
        loadConfirmedBookings();
        formPanel.add(cbBooking, gbc);

        // Amount
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Amount (₹):"), gbc);
        gbc.gridx = 1;
        tfAmount = new JTextField(15);
        tfAmount.setEditable(false);   // auto-filled when booking is selected
        tfAmount.setBackground(new Color(235, 235, 235));
        formPanel.add(tfAmount, gbc);

        // Payment Method
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Payment Method:"), gbc);
        gbc.gridx = 1;
        cbMethod = new JComboBox<>(new String[]{"Cash", "Credit Card", "Debit Card",
                                                "UPI", "Net Banking"});
        formPanel.add(cbMethod, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // ---- Buttons ----
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        btnPanel.setBackground(Color.WHITE);

        btnPay   = new JButton("Pay Now");
        btnClose = new JButton("Cancel");

        btnPay.setBackground(new Color(30, 130, 60));
        btnPay.setForeground(Color.WHITE);
        btnPay.setFocusPainted(false);
        btnPay.setPreferredSize(new Dimension(110, 35));

        btnPanel.add(btnPay);
        btnPanel.add(btnClose);
        mainPanel.add(btnPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // ---- Events ----
        cbBooking.addActionListener(e -> fillAmount());
        btnPay.addActionListener(e -> doPayment());
        btnClose.addActionListener(e -> dispose());

        fillAmount(); // fill immediately for first selection
    }

    private void loadConfirmedBookings() {
        confirmedBookings = bookingDAO.getBookingsByUser(currentUser.getUserId());
        cbBooking.removeAllItems();
        boolean anyConfirmed = false;
        for (Booking b : confirmedBookings) {
            if ("Confirmed".equalsIgnoreCase(b.getBookingStatus())) {
                cbBooking.addItem("Booking #" + b.getBookingId()
                        + " | Room " + b.getRoomNumber()
                        + " | ₹" + String.format("%.2f", b.getTotalAmount()));
                anyConfirmed = true;
            }
        }
        if (!anyConfirmed) {
            cbBooking.addItem("No confirmed bookings");
        }
    }

    /** Auto-fill amount from selected booking */
    private void fillAmount() {
        int idx = cbBooking.getSelectedIndex();
        if (idx < 0 || confirmedBookings.isEmpty()) return;

        // Filter only confirmed ones (mirroring loadConfirmedBookings)
        int confirmed = 0;
        for (Booking b : confirmedBookings) {
            if ("Confirmed".equalsIgnoreCase(b.getBookingStatus())) {
                if (confirmed == idx) {
                    tfAmount.setText(String.format("%.2f", b.getTotalAmount()));
                    return;
                }
                confirmed++;
            }
        }
    }

    private void doPayment() {
        int idx = cbBooking.getSelectedIndex();
        if (idx < 0 || confirmedBookings.isEmpty() || cbBooking.getItemAt(0).toString().startsWith("No")) {
            JOptionPane.showMessageDialog(this,
                "No confirmed bookings available for payment.");
            return;
        }

        // Get selected confirmed booking
        int confirmed = 0;
        Booking selected = null;
        for (Booking b : confirmedBookings) {
            if ("Confirmed".equalsIgnoreCase(b.getBookingStatus())) {
                if (confirmed == idx) { selected = b; break; }
                confirmed++;
            }
        }
        if (selected == null) return;

        // Check if payment already exists
        Payment existing = paymentDAO.getByBookingId(selected.getBookingId());
        if (existing != null) {
            JOptionPane.showMessageDialog(this,
                "Payment already recorded for this booking.", "Info",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String method = (String) cbMethod.getSelectedItem();
        double amount = selected.getTotalAmount();
        Date   today  = new Date(System.currentTimeMillis());

        Payment payment = new Payment(
            selected.getBookingId(), amount, today, method, "Paid"
        );

        boolean success = paymentDAO.add(payment);
        if (success) {
            JOptionPane.showMessageDialog(this,
                "Payment of ₹" + String.format("%.2f", amount) + " recorded successfully!\n"
                + "Method: " + method + "\n"
                + "Date: " + today,
                "Payment Successful", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                "Payment failed. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
