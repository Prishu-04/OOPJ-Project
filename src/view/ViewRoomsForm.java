package view;

import dao.RoomDAO;
import model.Room;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * ViewRoomsForm - Customer can view and filter available rooms.
 */
public class ViewRoomsForm extends JFrame {

    private JTable            table;
    private DefaultTableModel tableModel;
    private JComboBox<String> cbType, cbStatus;
    private JButton           btnSearch, btnClear, btnClose;

    private RoomDAO roomDAO = new RoomDAO();

    public ViewRoomsForm() {
        setTitle("View Rooms");
        setSize(680, 460);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        initUI();
        loadRooms(null, null);
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // ---- Filter bar ----
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        filterPanel.setBackground(new Color(230, 240, 255));
        filterPanel.setBorder(BorderFactory.createTitledBorder("Filter Rooms"));

        filterPanel.add(new JLabel("Type:"));
        cbType = new JComboBox<>(new String[]{"All","Single","Double","Suite","Deluxe"});
        filterPanel.add(cbType);

        filterPanel.add(new JLabel("  Status:"));
        cbStatus = new JComboBox<>(new String[]{"All","Available","Booked","Maintenance"});
        filterPanel.add(cbStatus);

        btnSearch = new JButton("Search");
        btnSearch.setBackground(new Color(30, 100, 180));
        btnSearch.setForeground(Color.WHITE);
        btnSearch.setFocusPainted(false);

        btnClear = new JButton("Clear");

        filterPanel.add(btnSearch);
        filterPanel.add(btnClear);

        mainPanel.add(filterPanel, BorderLayout.NORTH);

        // ---- Table ----
        String[] cols = {"ID", "Room No", "Type", "Price / Night (₹)", "Status"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        table.setRowHeight(24);

        mainPanel.add(new JScrollPane(table), BorderLayout.CENTER);

        // ---- Bottom ----
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnClose = new JButton("Close");
        bottomPanel.add(btnClose);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // ---- Events ----
        btnSearch.addActionListener(e -> {
            String type   = (String) cbType.getSelectedItem();
            String status = (String) cbStatus.getSelectedItem();
            loadRooms(type, status);
        });

        btnClear.addActionListener(e -> {
            cbType.setSelectedIndex(0);
            cbStatus.setSelectedIndex(0);
            loadRooms(null, null);
        });

        btnClose.addActionListener(e -> dispose());
    }

    private void loadRooms(String type, String status) {
        tableModel.setRowCount(0);
        List<Room> rooms = roomDAO.searchRooms(type, status);
        for (Room r : rooms) {
            tableModel.addRow(new Object[]{
                r.getRoomId(), r.getRoomNumber(), r.getRoomType(),
                String.format("%.2f", r.getPrice()), r.getStatus()
            });
        }
        if (rooms.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "No rooms found for the selected filter.", "No Results",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
