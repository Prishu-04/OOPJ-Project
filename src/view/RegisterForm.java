package view;

import dao.UserDAO;

import javax.swing.*;
import java.awt.*;

/**
 * RegisterForm - New customer registration screen.
 */
public class RegisterForm extends JFrame {

    private JTextField     tfName;
    private JTextField     tfEmail;
    private JTextField     tfPhone;
    private JPasswordField pfPassword;
    private JPasswordField pfConfirm;

    private final UserDAO userDAO = new UserDAO();

    private static final Color BG      = new Color(245, 247, 250);
    private static final Color PRIMARY = new Color(41,  128, 185);
    private static final Color ACCENT  = new Color(39,  174, 96);
    private static final Color DARK    = new Color(44,  62,  80);
    private static final Color WHITE   = Color.WHITE;

    public RegisterForm() {
        initUI();
    }

    private void initUI() {
        setTitle("Hotel Reservation System — Register");
        setSize(460, 460);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(BG);

        // Header
        JPanel header = new JPanel(new GridBagLayout());
        header.setBackground(PRIMARY);
        header.setPreferredSize(new Dimension(460, 70));
        JLabel title = new JLabel("Create New Account");
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        title.setForeground(WHITE);
        header.add(title);
        main.add(header, BorderLayout.NORTH);

        // Form
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(BG);
        form.setBorder(BorderFactory.createEmptyBorder(20, 40, 10, 40));
        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.insets = new Insets(7, 5, 7, 5);

        // Full Name
        g.gridx = 0; g.gridy = 0; form.add(lbl("Full Name:"), g);
        g.gridx = 1; tfName = tf(); form.add(tfName, g);

        // Email
        g.gridx = 0; g.gridy = 1; form.add(lbl("Email:"), g);
        g.gridx = 1; tfEmail = tf(); form.add(tfEmail, g);

        // Phone
        g.gridx = 0; g.gridy = 2; form.add(lbl("Phone:"), g);
        g.gridx = 1; tfPhone = tf(); form.add(tfPhone, g);

        // Password
        g.gridx = 0; g.gridy = 3; form.add(lbl("Password:"), g);
        g.gridx = 1; pfPassword = pf(); form.add(pfPassword, g);

        // Confirm Password
        g.gridx = 0; g.gridy = 4; form.add(lbl("Confirm Password:"), g);
        g.gridx = 1; pfConfirm = pf(); form.add(pfConfirm, g);

        // Register button
        g.gridx = 0; g.gridy = 5; g.gridwidth = 2;
        JButton btnReg = btn("Register", ACCENT);
        form.add(btnReg, g);

        // Back to login
        g.gridy = 6;
        JButton btnBack = new JButton("← Back to Login");
        btnBack.setBorderPainted(false);
        btnBack.setContentAreaFilled(false);
        btnBack.setForeground(PRIMARY);
        btnBack.setFont(new Font("SansSerif", Font.PLAIN, 12));
        btnBack.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        form.add(btnBack, g);

        main.add(form, BorderLayout.CENTER);
        add(main);

        getRootPane().setDefaultButton(btnReg);
        btnReg.addActionListener(e -> handleRegister());
        btnBack.addActionListener(e -> {
            new LoginForm().setVisible(true);
            dispose();
        });
    }

    private void handleRegister() {
        String name     = tfName.getText().trim();
        String email    = tfEmail.getText().trim();
        String phone    = tfPhone.getText().trim();
        String password = new String(pfPassword.getPassword()).trim();
        String confirm  = new String(pfConfirm.getPassword()).trim();

        // ── Validation ────────────────────────────────────
        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "All fields are required.", "Validation Error",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!email.matches("^[\\w._%+\\-]+@[\\w.\\-]+\\.[a-zA-Z]{2,}$")) {
            JOptionPane.showMessageDialog(this,
                "Please enter a valid email address.", "Validation Error",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!phone.matches("\\d{10}")) {
            JOptionPane.showMessageDialog(this,
                "Phone number must be 10 digits.", "Validation Error",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (password.length() < 6) {
            JOptionPane.showMessageDialog(this,
                "Password must be at least 6 characters.", "Validation Error",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!password.equals(confirm)) {
            JOptionPane.showMessageDialog(this,
                "Passwords do not match.", "Validation Error",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (userDAO.emailExists(email)) {
            JOptionPane.showMessageDialog(this,
                "This email is already registered.", "Duplicate Email",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean success = userDAO.registerUser(name, email, phone, password);
        if (success) {
            JOptionPane.showMessageDialog(this,
                "Registration successful! Please login.", "Success",
                JOptionPane.INFORMATION_MESSAGE);
            new LoginForm().setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                "Registration failed. Please try again.", "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    // ── Helpers ───────────────────────────────────────────

    private JLabel lbl(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("SansSerif", Font.BOLD, 13));
        l.setForeground(DARK);
        return l;
    }

    private JTextField tf() {
        JTextField t = new JTextField();
        styleComp(t);
        return t;
    }

    private JPasswordField pf() {
        JPasswordField p = new JPasswordField();
        styleComp(p);
        return p;
    }

    private void styleComp(JComponent c) {
        c.setPreferredSize(new Dimension(200, 30));
        c.setFont(new Font("SansSerif", Font.PLAIN, 13));
        c.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199)),
            BorderFactory.createEmptyBorder(3, 7, 3, 7)
        ));
    }

    private JButton btn(String text, Color bg) {
        JButton b = new JButton(text);
        b.setBackground(bg);
        b.setForeground(WHITE);
        b.setFont(new Font("SansSerif", Font.BOLD, 14));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setPreferredSize(new Dimension(200, 36));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }
}
