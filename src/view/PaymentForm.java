package view;

import dao.PaymentDAO;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

/**
 * PaymentForm - Collect payment details after a booking is confirmed.
 * Inserts a record into the payments table.
 */
public class PaymentForm extends JFrame {

    private JComboBox<String> cbMethod;
    private JLabel            lblAmount;
    private JLabel            lblBookingId;

    private final int        bookingId;
    private final double     amount;
    private final PaymentDAO paymentDAO = new PaymentDAO();

    private static final Color BG    = new Color(245, 247, 250);
    private static final Color HDR   = new Color(41,  128, 185);
    private static final Color GRN   = new Color(39,  174, 96);
    private static final Color WHITE = Color.WHITE;
    private static final Color DARK  = new Color(44,  62,  80);

    public PaymentForm(int bookingId, double amount) {
        this.bookingId = bookingId;
        this.amount    = amount;
        initUI();
    }

    private void initUI() {
        setTitle("Payment — Booking #" + bookingId);
        setSize(430, 390);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(BG);

        // Header
        JPanel header = new JPanel(new GridBagLayout());
        header.setBackground(HDR);
        header.setPreferredSize(new Dimension(430, 60));
        JLabel title = new JLabel("💳  Payment Gateway");
        title.setFont(new Font("SansSerif", Font.BOLD, 17));
        title.setForeground(WHITE);
        header.add(title);
        main.add(header, BorderLayout.NORTH);

        // Form
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(BG);
        form.setBorder(BorderFactory.createEmptyBorder(25, 45, 15, 45));
        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.insets = new Insets(10, 5, 10, 5);

        // Booking ID
        g.gridx=0; g.gridy=0; form.add(lbl("Booking ID:"), g);
        g.gridx=1;
        lblBookingId = new JLabel(String.valueOf(bookingId));
        lblBookingId.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblBookingId.setForeground(HDR);
        form.add(lblBookingId, g);

        // Amount
        g.gridx=0; g.gridy=1; form.add(lbl("Amount Payable:"), g);
        g.gridx=1;
        lblAmount = new JLabel("₹ " + String.format("%.2f", amount));
        lblAmount.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblAmount.setForeground(new Color(192, 57, 43));
        form.add(lblAmount, g);

        // Divider
        g.gridx=0; g.gridy=2; g.gridwidth=2;
        JSeparator sep = new JSeparator();
        form.add(sep, g);
        g.gridwidth=1;

        // Payment method
        g.gridx=0; g.gridy=3; form.add(lbl("Payment Method:"), g);
        g.gridx=1;
        cbMethod = new JComboBox<>(new String[]{
            "Cash", "Credit Card", "Debit Card", "UPI", "Net Banking"
        });
        cbMethod.setFont(new Font("SansSerif", Font.PLAIN, 13));
        cbMethod.setPreferredSize(new Dimension(180, 30));
        form.add(cbMethod, g);

        // Date info
        g.gridx=0; g.gridy=4; form.add(lbl("Payment Date:"), g);
        g.gridx=1;
        JLabel lblDate = new JLabel(LocalDate.now().toString());
        lblDate.setFont(new Font("SansSerif", Font.PLAIN, 13));
        form.add(lblDate, g);

        // Pay button
        g.gridx=0; g.gridy=5; g.gridwidth=2;
        JButton btnPay = new JButton("✅  Complete Payment");
        btnPay.setBackground(GRN);
        btnPay.setForeground(WHITE);
        btnPay.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnPay.setFocusPainted(false);
        btnPay.setBorderPainted(false);
        btnPay.setPreferredSize(new Dimension(220, 40));
        btnPay.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        form.add(btnPay, g);

        // Skip button
        g.gridy=6;
        JButton btnSkip = new JButton("Pay Later / Close");
        btnSkip.setBorderPainted(false);
        btnSkip.setContentAreaFilled(false);
        btnSkip.setForeground(Color.GRAY);
        btnSkip.setFont(new Font("SansSerif", Font.PLAIN, 12));
        btnSkip.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        form.add(btnSkip, g);

        main.add(form, BorderLayout.CENTER);
        add(main);

        getRootPane().setDefaultButton(btnPay);
        btnPay.addActionListener(e  -> handlePayment());
        btnSkip.addActionListener(e -> dispose());
    }

    private void handlePayment() {
        String method    = (String) cbMethod.getSelectedItem();
        String todayStr  = LocalDate.now().toString();

        boolean ok = paymentDAO.addPayment(bookingId, amount, todayStr, method);

        if (ok) {
            // ── Receipt dialog ─────────────────────────────
            String receipt =
                "╔═══════════════════════════════╗\n" +
                "║     PAYMENT RECEIPT           ║\n" +
                "╠═══════════════════════════════╣\n" +
                "  Booking ID   : " + bookingId      + "\n" +
                "  Amount Paid  : ₹" + String.format("%.2f", amount) + "\n" +
                "  Method       : " + method          + "\n" +
                "  Date         : " + todayStr        + "\n" +
                "  Status       : PAID ✅\n" +
                "╚═══════════════════════════════╝\n" +
                "\n  Thank you for choosing our hotel!";

            JOptionPane.showMessageDialog(this, receipt,
                "Payment Successful", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                "Payment recording failed. Please try again.", "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private JLabel lbl(String t) {
        JLabel l = new JLabel(t);
        l.setFont(new Font("SansSerif", Font.BOLD, 13));
        l.setForeground(DARK);
        return l;
    }
}
