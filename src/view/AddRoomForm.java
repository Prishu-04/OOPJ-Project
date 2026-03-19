package view;

import dao.RoomDAO;
import model.Room;

import javax.swing.*;
import java.awt.*;

/**
 * AddRoomForm - Admin can add a new room.
 */
public class AddRoomForm extends JFrame {

    private JTextField  tfRoomNumber, tfPrice;
    private JComboBox<String> cbRoomType, cbStatus;
    private JButton     btnAdd, btnClose;

    private RoomDAO roomDAO = new RoomDAO();

    public AddRoomForm() {
        setTitle("Add New Room");
        setSize(380, 320);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        initUI();
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 15, 25));
        mainPanel.setBackground(Color.WHITE);

        JLabel lblTitle = new JLabel("Add New Room", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitle.setForeground(new Color(30, 80, 150));
        mainPanel.add(lblTitle, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets  = new Insets(8, 5, 8, 5);
        gbc.fill    = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        // Room Number
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Room Number:"), gbc);
        gbc.gridx = 1;
        tfRoomNumber = new JTextField(15);
        formPanel.add(tfRoomNumber, gbc);

        // Room Type
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Room Type:"), gbc);
        gbc.gridx = 1;
        cbRoomType = new JComboBox<>(new String[]{"Single", "Double", "Suite", "Deluxe"});
        formPanel.add(cbRoomType, gbc);

        // Price
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Price (per night ₹):"), gbc);
        gbc.gridx = 1;
        tfPrice = new JTextField(15);
        formPanel.add(tfPrice, gbc);

        // Status
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Status:"), gbc);
        gbc.gridx = 1;
        cbStatus = new JComboBox<>(new String[]{"Available", "Maintenance"});
        formPanel.add(cbStatus, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // ---- Buttons ----
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        btnPanel.setBackground(Color.WHITE);

        btnAdd = new JButton("Add Room");
        btnAdd.setBackground(new Color(30, 100, 180));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setFocusPainted(false);

        btnClose = new JButton("Close");

        btnPanel.add(btnAdd);
        btnPanel.add(btnClose);
        mainPanel.add(btnPanel, BorderLayout.SOUTH);
        add(mainPanel);

        // ---- Events ----
        btnAdd.addActionListener(e -> addRoom());
        btnClose.addActionListener(e -> dispose());
    }

    private void addRoom() {
        String roomNumber = tfRoomNumber.getText().trim();
        String roomType   = (String) cbRoomType.getSelectedItem();
        String priceStr   = tfPrice.getText().trim();
        String status     = (String) cbStatus.getSelectedItem();

        if (roomNumber.isEmpty() || priceStr.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Room Number and Price are required.", "Validation Error",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceStr);
            if (price <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                "Please enter a valid positive price.", "Validation Error",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        Room newRoom = new Room(roomNumber, roomType, price, status);
        boolean success = roomDAO.add(newRoom);

        if (success) {
            JOptionPane.showMessageDialog(this,
                "Room " + roomNumber + " added successfully!", "Success",
                JOptionPane.INFORMATION_MESSAGE);
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this,
                "Failed to add room. Room number may already exist.", "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearForm() {
        tfRoomNumber.setText("");
        tfPrice.setText("");
        cbRoomType.setSelectedIndex(0);
        cbStatus.setSelectedIndex(0);
    }
}
