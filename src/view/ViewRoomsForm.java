package view;

import dao.RoomDAO;
import model.Room;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * ViewRoomsForm - Displays rooms for admin (all rooms) and customers (available only).
 * Supports search/filter by room type.
 */
public class ViewRoomsForm extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField tfSearch;
    private JComboBox<String> cbFilter;

    private final RoomDAO  roomDAO;
    private final boolean  isAdmin;

    private static final Color BG    = new Color(245, 247, 250);
    private static final Color HDR   = new Color(41,  128, 185);
    private static final Color WHITE = Color.WHITE;

    public ViewRoomsForm(boolean isAdmin) {
        this.isAdmin = isAdmin;
        this.roomDAO = new RoomDAO();
        initUI();
        loadRooms(null, null);
    }

    private void initUI() {
        setTitle(isAdmin ? "All Rooms" : "Available Rooms");
        setSize(700, 460);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(BG);

        // Header
        JPanel header = new JPanel(new GridBagLayout());
        header.setBackground(HDR);
        header.setPreferredSize(new Dimension(700, 55));
        JLabel title = new JLabel(isAdmin ? "🛏️  All Hotel Rooms" : "🛏️  Available Rooms");
        title.setFont(new Font("SansSerif", Font.BOLD, 16));
        title.setForeground(WHITE);
        header.add(title);
        main.add(header, BorderLayout.NORTH);

        // ── Search bar ────────────────────────────────────
        JPanel searchBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 8));
        searchBar.setBackground(BG);

        searchBar.add(new JLabel("Search:"));
        tfSearch = new JTextField(12);
        tfSearch.setFont(new Font("SansSerif", Font.PLAIN, 13));
        searchBar.add(tfSearch);

        searchBar.add(new JLabel("Filter by Type:"));
        cbFilter = new JComboBox<>(new String[]{
            "All", "Single", "Double", "Suite", "Deluxe", "Family"
        });
        cbFilter.setFont(new Font("SansSerif", Font.PLAIN, 13));
        searchBar.add(cbFilter);

        JButton btnSearch = new JButton("🔍 Search");
        btnSearch.setBackground(HDR);
        btnSearch.setForeground(WHITE);
        btnSearch.setFocusPainted(false);
        btnSearch.setBorderPainted(false);
        btnSearch.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        searchBar.add(btnSearch);

        JButton btnAll = new JButton("Show All");
        btnAll.setFont(new Font("SansSerif", Font.PLAIN, 12));
        btnAll.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        searchBar.add(btnAll);

        main.add(searchBar, BorderLayout.BEFORE_FIRST_LINE); // replaces later

        // ── Table ─────────────────────────────────────────
        String[] cols = {"Room ID", "Room No", "Type", "Price / Night (₹)", "Status"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));
        table.setRowHeight(26);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        table.getTableHeader().setBackground(HDR);
        table.getTableHeader().setForeground(WHITE);
        table.setSelectionBackground(new Color(174, 214, 241));
        table.setGridColor(new Color(220, 220, 220));

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

        // Combine search + table
        JPanel centre = new JPanel(new BorderLayout());
        centre.setBackground(BG);
        centre.add(searchBar, BorderLayout.NORTH);
        centre.add(scroll, BorderLayout.CENTER);
        main.add(centre, BorderLayout.CENTER);

        // Footer info label
        JLabel info = new JLabel(
            isAdmin ? "Admin view: all rooms shown."
                    : "Showing available rooms. Select a room and click Book.",
            SwingConstants.CENTER
        );
        info.setFont(new Font("SansSerif", Font.ITALIC, 11));
        info.setForeground(Color.GRAY);
        info.setBorder(BorderFactory.createEmptyBorder(5, 0, 8, 0));
        main.add(info, BorderLayout.SOUTH);

        add(main);

        // Listeners
        btnSearch.addActionListener(e -> {
            String keyword = tfSearch.getText().trim();
            String type    = cbFilter.getSelectedItem().toString();
            loadRooms("All".equals(type) ? null : type, keyword.isEmpty() ? null : keyword);
        });
        btnAll.addActionListener(e -> { tfSearch.setText(""); cbFilter.setSelectedIndex(0); loadRooms(null, null); });
    }

    private void loadRooms(String type, String keyword) {
        tableModel.setRowCount(0);
        List<Room> rooms;

        if (type != null) {
            rooms = roomDAO.searchRoomsByType(type);
        } else if (isAdmin) {
            rooms = roomDAO.getAllRooms();
        } else {
            rooms = roomDAO.getAvailableRooms();
        }

        for (Room r : rooms) {
            // If customer typed keyword, filter client-side
            if (keyword != null && !r.getRoomNumber().contains(keyword)
                    && !r.getRoomType().toLowerCase().contains(keyword.toLowerCase())) {
                continue;
            }
            tableModel.addRow(new Object[]{
                r.getRoomId(), r.getRoomNumber(), r.getRoomType(),
                String.format("%.2f", r.getPrice()), r.getStatus()
            });
        }
    }
}
