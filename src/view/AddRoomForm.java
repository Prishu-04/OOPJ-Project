package view;

import dao.RoomDAO;

import javax.swing.*;
import java.awt.*;

/**
 * AddRoomForm - Admin form to add a new hotel room.
 */
public class AddRoomForm extends JFrame {

    private JTextField tfRoomNumber;
    private JComboBox<String> cbRoomType;
    private JTextField tfPrice;
    private JComboBox<String> cbStatus;

    private final RoomDAO roomDAO = new RoomDAO();

    private static final Color BG     = new Color(245, 247, 250);
    private static final Color HDR    = new Color(44,  62,  80);
    private static final Color GRN    = new Color(39,  174, 96);
    private static final Color WHITE  = Color.WHITE;
    private static final Color DARK   = new Color(44,  62,  80);

    public AddRoomForm() {
        initUI();
    }

    private void initUI() {
        setTitle("Add New Room");
        setSize(420, 380);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(BG);

        // Header
        JPanel header = new JPanel(new GridBagLayout());
        header.setBackground(HDR);
        header.setPreferredSize(new Dimension(420, 60));
        JLabel title = new JLabel("➕  Add New Room");
        title.setFont(new Font("SansSerif", Font.BOLD, 17));
        title.setForeground(WHITE);
        header.add(title);
        main.add(header, BorderLayout.NORTH);

        // Form
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(BG);
        form.setBorder(BorderFactory.createEmptyBorder(20, 40, 10, 40));
        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.insets = new Insets(8, 5, 8, 5);

        // Room Number
        g.gridx=0; g.gridy=0; form.add(lbl("Room Number:"), g);
        g.gridx=1; tfRoomNumber = tf(); form.add(tfRoomNumber, g);

        // Room Type
        g.gridx=0; g.gridy=1; form.add(lbl("Room Type:"), g);
        g.gridx=1;
        cbRoomType = new JComboBox<>(new String[]{
            "Single", "Double", "Suite", "Deluxe", "Family"
        });
        styleCombo(cbRoomType);
        form.add(cbRoomType, g);

        // Price per night
        g.gridx=0; g.gridy=2; form.add(lbl("Price / Night (₹):"), g);
        g.gridx=1; tfPrice = tf(); form.add(tfPrice, g);

        // Status
        g.gridx=0; g.gridy=3; form.add(lbl("Status:"), g);
        g.gridx=1;
        cbStatus = new JComboBox<>(new String[]{"Available", "Booked", "Maintenance"});
        styleCombo(cbStatus);
        form.add(cbStatus, g);

        // Buttons row
        g.gridx=0; g.gridy=4; g.gridwidth=2;
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        btnRow.setBackground(BG);

        JButton btnSave = btn("Save Room", GRN);
        JButton btnClear = btn("Clear", new Color(149, 165, 166));
        btnRow.add(btnSave);
        btnRow.add(btnClear);
        form.add(btnRow, g);

        main.add(form, BorderLayout.CENTER);
        add(main);

        btnSave.addActionListener(e -> handleSave());
        btnClear.addActionListener(e -> clearFields());
        getRootPane().setDefaultButton(btnSave);
    }

    private void handleSave() {
        String roomNumber = tfRoomNumber.getText().trim();
        String roomType   = (String) cbRoomType.getSelectedItem();
        String priceStr   = tfPrice.getText().trim();
        String status     = (String) cbStatus.getSelectedItem();

        if (roomNumber.isEmpty() || priceStr.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Room number and price are required.", "Validation Error",
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

        boolean ok = roomDAO.addRoom(roomNumber, roomType, price, status);
        if (ok) {
            JOptionPane.showMessageDialog(this,
                "Room added successfully!", "Success",
                JOptionPane.INFORMATION_MESSAGE);
            clearFields();
        } else {
            JOptionPane.showMessageDialog(this,
                "Failed to add room. Room number may already exist.", "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        tfRoomNumber.setText("");
        tfPrice.setText("");
        cbRoomType.setSelectedIndex(0);
        cbStatus.setSelectedIndex(0);
    }

    // ── Helpers ───────────────────────────────────────────

    private JLabel lbl(String t) {
        JLabel l = new JLabel(t);
        l.setFont(new Font("SansSerif", Font.BOLD, 13));
        l.setForeground(DARK);
        return l;
    }

    private JTextField tf() {
        JTextField t = new JTextField();
        t.setPreferredSize(new Dimension(180, 30));
        t.setFont(new Font("SansSerif", Font.PLAIN, 13));
        t.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199)),
            BorderFactory.createEmptyBorder(3, 7, 3, 7)
        ));
        return t;
    }

    private void styleCombo(JComboBox<String> cb) {
        cb.setPreferredSize(new Dimension(180, 30));
        cb.setFont(new Font("SansSerif", Font.PLAIN, 13));
    }

    private JButton btn(String text, Color bg) {
        JButton b = new JButton(text);
        b.setBackground(bg);
        b.setForeground(WHITE);
        b.setFont(new Font("SansSerif", Font.BOLD, 13));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setPreferredSize(new Dimension(130, 34));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }
}
