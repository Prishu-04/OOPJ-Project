package view;

import dao.RoomDAO;
import model.Room;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * ManageRoomsForm - Admin view to edit or delete existing rooms.
 * Uses JTable for display.
 */
public class ManageRoomsForm extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;

    private final RoomDAO roomDAO = new RoomDAO();

    private static final Color BG    = new Color(245, 247, 250);
    private static final Color HDR   = new Color(44,  62,  80);
    private static final Color BLU   = new Color(41,  128, 185);
    private static final Color RED   = new Color(192, 57,  43);
    private static final Color GRN   = new Color(39,  174, 96);
    private static final Color WHITE = Color.WHITE;

    public ManageRoomsForm() {
        initUI();
        loadRooms();
    }

    private void initUI() {
        setTitle("Manage Rooms");
        setSize(720, 480);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(BG);

        // Header
        JPanel header = new JPanel(new GridBagLayout());
        header.setBackground(HDR);
        header.setPreferredSize(new Dimension(720, 55));
        JLabel title = new JLabel("✏️  Manage Rooms");
        title.setFont(new Font("SansSerif", Font.BOLD, 16));
        title.setForeground(WHITE);
        header.add(title);
        main.add(header, BorderLayout.NORTH);

        // Table
        String[] cols = {"ID", "Room No", "Type", "Price (₹)", "Status"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));
        table.setRowHeight(26);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        table.getTableHeader().setBackground(BLU);
        table.getTableHeader().setForeground(WHITE);
        table.setSelectionBackground(new Color(174, 214, 241));
        table.setGridColor(new Color(220, 220, 220));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Column widths
        table.getColumnModel().getColumn(0).setPreferredWidth(40);
        table.getColumnModel().getColumn(1).setPreferredWidth(80);
        table.getColumnModel().getColumn(2).setPreferredWidth(100);
        table.getColumnModel().getColumn(3).setPreferredWidth(100);
        table.getColumnModel().getColumn(4).setPreferredWidth(100);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder(10, 15, 5, 15));
        main.add(scroll, BorderLayout.CENTER);

        // Button bar
        JPanel btnBar = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        btnBar.setBackground(BG);

        JButton btnEdit   = btn("✏️ Edit Selected",   BLU);
        JButton btnDelete = btn("🗑️ Delete Selected",  RED);
        JButton btnToggle = btn("🔄 Toggle Status",    GRN);
        JButton btnRefresh= btn("🔃 Refresh",          new Color(127, 140, 141));

        btnBar.add(btnEdit);
        btnBar.add(btnDelete);
        btnBar.add(btnToggle);
        btnBar.add(btnRefresh);
        main.add(btnBar, BorderLayout.SOUTH);

        add(main);

        btnEdit.addActionListener(e -> editSelected());
        btnDelete.addActionListener(e -> deleteSelected());
        btnToggle.addActionListener(e -> toggleStatus());
        btnRefresh.addActionListener(e -> loadRooms());
    }

    // ── Load all rooms into table ──────────────────────────
    private void loadRooms() {
        tableModel.setRowCount(0);
        List<Room> rooms = roomDAO.getAllRooms();
        for (Room r : rooms) {
            tableModel.addRow(new Object[]{
                r.getRoomId(), r.getRoomNumber(), r.getRoomType(),
                String.format("%.2f", r.getPrice()), r.getStatus()
            });
        }
    }

    // ── Edit selected row ─────────────────────────────────
    private void editSelected() {
        int row = table.getSelectedRow();
        if (row < 0) { noSelection(); return; }

        int    roomId     = (int)    tableModel.getValueAt(row, 0);
        String roomNumber = (String) tableModel.getValueAt(row, 1);
        String roomType   = (String) tableModel.getValueAt(row, 2);
        double price      = Double.parseDouble(
                                tableModel.getValueAt(row, 3).toString());
        String status     = (String) tableModel.getValueAt(row, 4);

        // Inline edit dialog
        JTextField tfNum   = new JTextField(roomNumber);
        String[]   types   = {"Single","Double","Suite","Deluxe","Family"};
        JComboBox<String> cbType = new JComboBox<>(types);
        cbType.setSelectedItem(roomType);
        JTextField tfPrice = new JTextField(String.valueOf(price));
        String[] statuses = {"Available","Booked","Maintenance"};
        JComboBox<String> cbStatus = new JComboBox<>(statuses);
        cbStatus.setSelectedItem(status);

        JPanel panel = new JPanel(new GridLayout(4, 2, 5, 8));
        panel.add(new JLabel("Room Number:")); panel.add(tfNum);
        panel.add(new JLabel("Room Type:"));   panel.add(cbType);
        panel.add(new JLabel("Price (₹):"));   panel.add(tfPrice);
        panel.add(new JLabel("Status:"));      panel.add(cbStatus);

        int result = JOptionPane.showConfirmDialog(this, panel,
            "Edit Room #" + roomId, JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                double newPrice = Double.parseDouble(tfPrice.getText().trim());
                boolean ok = roomDAO.updateRoom(
                    roomId, tfNum.getText().trim(),
                    (String) cbType.getSelectedItem(),
                    newPrice,
                    (String) cbStatus.getSelectedItem()
                );
                if (ok) { JOptionPane.showMessageDialog(this, "Room updated!"); loadRooms(); }
                else      JOptionPane.showMessageDialog(this, "Update failed.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid price.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // ── Delete selected row ───────────────────────────────
    private void deleteSelected() {
        int row = table.getSelectedRow();
        if (row < 0) { noSelection(); return; }

        int roomId = (int) tableModel.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
            "Delete Room ID " + roomId + "?", "Confirm Delete",
            JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean ok = roomDAO.deleteRoom(roomId);
            if (ok) { JOptionPane.showMessageDialog(this, "Room deleted."); loadRooms(); }
            else      JOptionPane.showMessageDialog(this, "Delete failed.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ── Toggle Available ↔ Booked ─────────────────────────
    private void toggleStatus() {
        int row = table.getSelectedRow();
        if (row < 0) { noSelection(); return; }

        int    roomId = (int)    tableModel.getValueAt(row, 0);
        String status = (String) tableModel.getValueAt(row, 4);
        String newStatus = "Available".equals(status) ? "Booked" : "Available";

        boolean ok = roomDAO.updateRoomStatus(roomId, newStatus);
        if (ok) { JOptionPane.showMessageDialog(this, "Status → " + newStatus); loadRooms(); }
    }

    private void noSelection() {
        JOptionPane.showMessageDialog(this, "Please select a room first.",
            "No Selection", JOptionPane.INFORMATION_MESSAGE);
    }

    private JButton btn(String text, Color bg) {
        JButton b = new JButton(text);
        b.setBackground(bg);
        b.setForeground(WHITE);
        b.setFont(new Font("SansSerif", Font.BOLD, 12));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(150, 32));
        return b;
    }
}
