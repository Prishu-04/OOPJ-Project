package view;

import dao.RoomDAO;
import model.Room;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * ManageRoomsForm - Admin views all rooms in a JTable and can update or delete them.
 */
public class ManageRoomsForm extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;
    private JButton btnUpdate, btnDelete, btnRefresh, btnClose;

    private RoomDAO roomDAO = new RoomDAO();

    public ManageRoomsForm() {
        setTitle("Manage Rooms");
        setSize(700, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        initUI();
        loadRooms();
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel lblTitle = new JLabel("All Rooms", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 16));
        mainPanel.add(lblTitle, BorderLayout.NORTH);

        // ---- Table ----
        String[] columns = {"ID", "Room No", "Type", "Price (₹)", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        table.setRowHeight(24);

        JScrollPane scrollPane = new JScrollPane(table);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // ---- Buttons ----
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 8));

        btnUpdate  = new JButton("Update Selected");
        btnDelete  = new JButton("Delete Selected");
        btnRefresh = new JButton("Refresh");
        btnClose   = new JButton("Close");

        styleBtn(btnUpdate,  new Color(30, 100, 180));
        styleBtn(btnDelete,  new Color(180, 40, 40));
        styleBtn(btnRefresh, new Color(80, 140, 80));

        btnPanel.add(btnUpdate);
        btnPanel.add(btnDelete);
        btnPanel.add(btnRefresh);
        btnPanel.add(btnClose);
        mainPanel.add(btnPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // ---- Events ----
        btnRefresh.addActionListener(e -> loadRooms());
        btnClose.addActionListener(e -> dispose());

        btnDelete.addActionListener(e -> deleteSelectedRoom());
        btnUpdate.addActionListener(e -> updateSelectedRoom());
    }

    private void loadRooms() {
        tableModel.setRowCount(0);
        List<Room> rooms = roomDAO.getAll();
        for (Room r : rooms) {
            tableModel.addRow(new Object[]{
                r.getRoomId(), r.getRoomNumber(), r.getRoomType(),
                String.format("%.2f", r.getPrice()), r.getStatus()
            });
        }
    }

    private void deleteSelectedRoom() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a room to delete.");
            return;
        }
        int roomId = (int) tableModel.getValueAt(row, 0);
        String roomNo = (String) tableModel.getValueAt(row, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
            "Delete Room " + roomNo + "? This cannot be undone.", "Confirm Delete",
            JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = roomDAO.delete(roomId);
            if (success) {
                JOptionPane.showMessageDialog(this, "Room deleted successfully.");
                loadRooms();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Could not delete room. It may have associated bookings.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void updateSelectedRoom() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a room to update.");
            return;
        }

        int    roomId     = (int)    tableModel.getValueAt(row, 0);
        String roomNumber = (String) tableModel.getValueAt(row, 1);
        String roomType   = (String) tableModel.getValueAt(row, 2);
        String priceStr   = (String) tableModel.getValueAt(row, 3);
        String status     = (String) tableModel.getValueAt(row, 4);
        double price      = Double.parseDouble(priceStr.replace(",", ""));

        // Build a small dialog for editing
        JTextField  tfNumber = new JTextField(roomNumber, 12);
        JComboBox<String> cbType = new JComboBox<>(new String[]{"Single","Double","Suite","Deluxe"});
        cbType.setSelectedItem(roomType);
        JTextField  tfPrice  = new JTextField(priceStr, 12);
        JComboBox<String> cbStatus = new JComboBox<>(new String[]{"Available","Booked","Maintenance"});
        cbStatus.setSelectedItem(status);

        JPanel panel = new JPanel(new GridLayout(4, 2, 6, 6));
        panel.add(new JLabel("Room Number:")); panel.add(tfNumber);
        panel.add(new JLabel("Room Type:"));   panel.add(cbType);
        panel.add(new JLabel("Price (₹):"));    panel.add(tfPrice);
        panel.add(new JLabel("Status:"));       panel.add(cbStatus);

        int result = JOptionPane.showConfirmDialog(this, panel, "Update Room",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String newNumber = tfNumber.getText().trim();
            String newType   = (String) cbType.getSelectedItem();
            String newStatus = (String) cbStatus.getSelectedItem();
            double newPrice;

            if (newNumber.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Room number cannot be empty.");
                return;
            }

            try {
                newPrice = Double.parseDouble(tfPrice.getText().trim());
                if (newPrice <= 0) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Enter a valid positive price.");
                return;
            }

            Room updated = new Room(roomId, newNumber, newType, newPrice, newStatus);
            boolean success = roomDAO.update(updated);
            if (success) {
                JOptionPane.showMessageDialog(this, "Room updated successfully.");
                loadRooms();
            } else {
                JOptionPane.showMessageDialog(this, "Update failed.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void styleBtn(JButton btn, Color bg) {
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
    }
}
