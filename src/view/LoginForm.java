package view;

import dao.UserDAO;
import model.User;

import javax.swing.*;
import java.awt.*;

/**
 * LoginForm - Entry point GUI.
 * Handles login for both Admin and Customer.
 */
public class LoginForm extends JFrame {

    private JTextField     tfEmail;
    private JPasswordField pfPassword;
    private JButton        btnLogin;
    private JButton        btnRegister;

    private final UserDAO userDAO = new UserDAO();

    // ── Color palette ──────────────────────────────────────
    private static final Color BG        = new Color(245, 247, 250);
    private static final Color PRIMARY   = new Color(41,  128, 185);
    private static final Color ACCENT    = new Color(52,  152, 219);
    private static final Color DARK      = new Color(44,  62,  80);
    private static final Color WHITE     = Color.WHITE;

    public LoginForm() {
        initUI();
    }

    private void initUI() {
        setTitle("Hotel Reservation System — Login");
        setSize(450, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // ── Main panel ────────────────────────────────────
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BG);

        // ── Header ────────────────────────────────────────
        JPanel header = new JPanel();
        header.setBackground(PRIMARY);
        header.setPreferredSize(new Dimension(450, 80));
        JLabel title = new JLabel("🏨  Hotel Reservation System");
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        title.setForeground(WHITE);
        header.setLayout(new GridBagLayout());
        header.add(title);
        mainPanel.add(header, BorderLayout.NORTH);

        // ── Form panel ────────────────────────────────────
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(BG);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 5, 8, 5);

        // Sub-title
        JLabel subTitle = new JLabel("Login to Your Account", SwingConstants.CENTER);
        subTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        subTitle.setForeground(DARK);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        formPanel.add(subTitle, gbc);

        // Email
        gbc.gridy = 1; gbc.gridwidth = 1;
        gbc.gridx = 0; formPanel.add(makeLabel("Email:"), gbc);
        gbc.gridx = 1;
        tfEmail = makeTextField();
        formPanel.add(tfEmail, gbc);

        // Password
        gbc.gridy = 2;
        gbc.gridx = 0; formPanel.add(makeLabel("Password:"), gbc);
        gbc.gridx = 1;
        pfPassword = new JPasswordField();
        styleField(pfPassword);
        formPanel.add(pfPassword, gbc);

        // Login button
        gbc.gridy = 3; gbc.gridx = 0; gbc.gridwidth = 2;
        btnLogin = makeButton("Login", ACCENT);
        formPanel.add(btnLogin, gbc);

        // Register link
        gbc.gridy = 4;
        btnRegister = new JButton("New customer? Register here");
        btnRegister.setBorderPainted(false);
        btnRegister.setContentAreaFilled(false);
        btnRegister.setForeground(PRIMARY);
        btnRegister.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnRegister.setFont(new Font("SansSerif", Font.PLAIN, 12));
        formPanel.add(btnRegister, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // ── Footer ────────────────────────────────────────
        JLabel footer = new JLabel(
            "Admin: admin@hotel.com / admin123", SwingConstants.CENTER);
        footer.setFont(new Font("SansSerif", Font.ITALIC, 11));
        footer.setForeground(Color.GRAY);
        footer.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        mainPanel.add(footer, BorderLayout.SOUTH);

        add(mainPanel);

        // ── Action Listeners ──────────────────────────────
        btnLogin.addActionListener(e -> handleLogin());
        btnRegister.addActionListener(e -> openRegister());

        // Allow Enter key to trigger login
        getRootPane().setDefaultButton(btnLogin);
    }

    private void handleLogin() {
        String email    = tfEmail.getText().trim();
        String password = new String(pfPassword.getPassword()).trim();

        // Validation
        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter both email and password.", "Validation Error",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        User user = userDAO.loginUser(email, password);

        if (user == null) {
            JOptionPane.showMessageDialog(this,
                "Invalid email or password. Please try again.", "Login Failed",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this,
            "Welcome, " + user.getName() + "!", "Login Successful",
            JOptionPane.INFORMATION_MESSAGE);

        // Redirect based on role (Polymorphism in action)
        if ("admin".equals(user.getRole())) {
            new AdminDashboard(user).setVisible(true);
        } else {
            new UserDashboard(user).setVisible(true);
        }
        dispose();
    }

    private void openRegister() {
        new RegisterForm().setVisible(true);
        dispose();
    }

    // ── UI Helpers ────────────────────────────────────────

    private JLabel makeLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 13));
        lbl.setForeground(DARK);
        return lbl;
    }

    private JTextField makeTextField() {
        JTextField tf = new JTextField();
        styleField(tf);
        return tf;
    }

    private void styleField(JComponent comp) {
        comp.setPreferredSize(new Dimension(200, 32));
        comp.setFont(new Font("SansSerif", Font.PLAIN, 13));
        comp.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199)),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
    }

    private JButton makeButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(WHITE);
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(200, 38));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }
}
