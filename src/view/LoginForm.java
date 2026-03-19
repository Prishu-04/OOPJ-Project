package view;

import dao.UserDAO;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * LoginForm - First screen of the application.
 * Both Admin and Customer log in from the same form.
 */
public class LoginForm extends JFrame {

    private JTextField     tfEmail;
    private JPasswordField pfPassword;
    private JButton        btnLogin, btnRegister;

    private UserDAO userDAO = new UserDAO();

    public LoginForm() {
        setTitle("Hotel Reservation System - Login");
        setSize(400, 320);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        initUI();
    }

    private void initUI() {
        // ---- Main panel ----
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        mainPanel.setBackground(new Color(245, 245, 245));

        // ---- Header ----
        JLabel lblTitle = new JLabel("Hotel Reservation System", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitle.setForeground(new Color(30, 80, 150));
        mainPanel.add(lblTitle, BorderLayout.NORTH);

        // ---- Form panel ----
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(245, 245, 245));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Email
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        tfEmail = new JTextField(20);
        formPanel.add(tfEmail, gbc);

        // Password
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        pfPassword = new JPasswordField(20);
        formPanel.add(pfPassword, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // ---- Buttons ----
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        btnPanel.setBackground(new Color(245, 245, 245));

        btnLogin = new JButton("Login");
        btnLogin.setBackground(new Color(30, 80, 150));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setPreferredSize(new Dimension(100, 35));

        btnRegister = new JButton("Register");
        btnRegister.setPreferredSize(new Dimension(100, 35));

        btnPanel.add(btnLogin);
        btnPanel.add(btnRegister);
        mainPanel.add(btnPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // ---- Event Listeners ----
        btnLogin.addActionListener(e -> doLogin());

        // Allow pressing Enter to login
        pfPassword.addActionListener(e -> doLogin());

        btnRegister.addActionListener(e -> {
            new RegisterForm().setVisible(true);
            dispose();
        });
    }

    private void doLogin() {
        String email    = tfEmail.getText().trim();
        String password = new String(pfPassword.getPassword()).trim();

        // Validation
        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter both email and password.", "Validation Error",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Database check
        User user = userDAO.login(email, password);

        if (user == null) {
            JOptionPane.showMessageDialog(this,
                "Invalid email or password. Please try again.", "Login Failed",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Route based on role
        if ("admin".equalsIgnoreCase(user.getRole())) {
            new AdminDashboard(user).setVisible(true);
        } else {
            new UserDashboard(user).setVisible(true);
        }
        dispose();
    }
}
