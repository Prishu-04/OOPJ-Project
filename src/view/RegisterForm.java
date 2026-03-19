package view;

import dao.UserDAO;
import model.User;

import javax.swing.*;
import java.awt.*;

/**
 * RegisterForm - New customer registration screen.
 */
public class RegisterForm extends JFrame {

    private JTextField     tfName, tfEmail, tfPhone;
    private JPasswordField pfPassword, pfConfirm;
    private JButton        btnRegister, btnBack;

    private UserDAO userDAO = new UserDAO();

    public RegisterForm() {
        setTitle("Register - Hotel Reservation System");
        setSize(430, 420);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        initUI();
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        mainPanel.setBackground(new Color(245, 245, 245));

        JLabel lblTitle = new JLabel("Customer Registration", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitle.setForeground(new Color(30, 80, 150));
        mainPanel.add(lblTitle, BorderLayout.NORTH);

        // ---- Form ----
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(245, 245, 245));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(7, 5, 7, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        String[] labels = {"Full Name:", "Email:", "Phone:", "Password:", "Confirm Password:"};
        gbc.gridx = 0; gbc.gridy = 0;
        for (String lbl : labels) {
            formPanel.add(new JLabel(lbl), gbc);
            gbc.gridy++;
        }

        tfName    = new JTextField(20);
        tfEmail   = new JTextField(20);
        tfPhone   = new JTextField(20);
        pfPassword = new JPasswordField(20);
        pfConfirm  = new JPasswordField(20);

        JComponent[] fields = {tfName, tfEmail, tfPhone, pfPassword, pfConfirm};
        gbc.gridx = 1; gbc.gridy = 0;
        for (JComponent f : fields) {
            formPanel.add(f, gbc);
            gbc.gridy++;
        }

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // ---- Buttons ----
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        btnPanel.setBackground(new Color(245, 245, 245));

        btnRegister = new JButton("Register");
        btnRegister.setBackground(new Color(30, 130, 60));
        btnRegister.setForeground(Color.WHITE);
        btnRegister.setFocusPainted(false);
        btnRegister.setPreferredSize(new Dimension(110, 35));

        btnBack = new JButton("Back to Login");
        btnBack.setPreferredSize(new Dimension(120, 35));

        btnPanel.add(btnRegister);
        btnPanel.add(btnBack);
        mainPanel.add(btnPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // ---- Events ----
        btnRegister.addActionListener(e -> doRegister());
        btnBack.addActionListener(e -> {
            new LoginForm().setVisible(true);
            dispose();
        });
    }

    private void doRegister() {
        String name     = tfName.getText().trim();
        String email    = tfEmail.getText().trim();
        String phone    = tfPhone.getText().trim();
        String password = new String(pfPassword.getPassword()).trim();
        String confirm  = new String(pfConfirm.getPassword()).trim();

        // ---- Validations ----
        if (name.isEmpty() || email.isEmpty() || phone.isEmpty()
                || password.isEmpty() || confirm.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "All fields are required.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!email.contains("@") || !email.contains(".")) {
            JOptionPane.showMessageDialog(this,
                "Please enter a valid email address.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (phone.length() < 10) {
            JOptionPane.showMessageDialog(this,
                "Phone number must be at least 10 digits.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (password.length() < 6) {
            JOptionPane.showMessageDialog(this,
                "Password must be at least 6 characters.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!password.equals(confirm)) {
            JOptionPane.showMessageDialog(this,
                "Passwords do not match.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Check duplicate email
        if (userDAO.emailExists(email)) {
            JOptionPane.showMessageDialog(this,
                "This email is already registered. Please use a different email.",
                "Duplicate Email", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Create user object and save
        User newUser = new User(name, email, phone, password, "customer");
        boolean success = userDAO.add(newUser);

        if (success) {
            JOptionPane.showMessageDialog(this,
                "Registration successful! You can now login.",
                "Success", JOptionPane.INFORMATION_MESSAGE);
            new LoginForm().setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                "Registration failed. Please try again.",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
