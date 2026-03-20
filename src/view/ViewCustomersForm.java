package view;

import dao.UserDAO;
import model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * ViewCustomersForm - Admin view of all registered customers.
 * Uses JTable for display.
 */
public class ViewCustomersForm extends JFrame {

    private JTable            table;
    private DefaultTableModel tableModel;
    private JTextField        tfSearch;

    private final UserDAO userDAO = new UserDAO();
    private List<User>    allCustomers;

    private static final Color BG    = new Color(245, 247, 250);
    private static final Color HDR   = new Color(142, 68,  173);
    private static final Color WHITE = Color.WHITE;

    public ViewCustomersForm() {
        initUI();
        loadCustomers();
    }

    private void initUI() {
        setTitle("View All Customers");
        setSize(680, 460);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(BG);

        // Header
        JPanel header = new JPanel(new GridBagLayout());
        header.setBackground(HDR);
        header.setPreferredSize(new Dimension(680, 55));
        JLabel title = new JLabel("👥  All Registered Customers");
        title.setFont(new Font("SansSerif", Font.BOLD, 16));
        title.setForeground(WHITE);
        header.add(title);
        main.add(header, BorderLayout.NORTH);

        // Search bar
        JPanel searchBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 8));
        searchBar.setBackground(BG);
        searchBar.add(new JLabel("Search by name/email:"));
        tfSearch = new JTextField(18);
        tfSearch.setFont(new Font("SansSerif", Font.PLAIN, 13));
        searchBar.add(tfSearch);
        JButton btnSearch = new JButton("🔍 Search");
        styleBtn(btnSearch, HDR);
        searchBar.add(btnSearch);
        JButton btnAll = new JButton("Show All");
        btnAll.setFont(new Font("SansSerif", Font.PLAIN, 12));
        btnAll.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        searchBar.add(btnAll);

        // Table
        String[] cols = {"User ID", "Name", "Email", "Phone", "Role"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));
        table.setRowHeight(26);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        table.getTableHeader().setBackground(HDR);
        table.getTableHeader().setForeground(WHITE);
        table.setSelectionBackground(new Color(215, 189, 226));
        table.setGridColor(new Color(220, 220, 220));

        int[] widths = {60, 150, 200, 120, 80};
        for (int i = 0; i < widths.length; i++)
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);

        JScrollPane scroll = new JScrollPane(table);

        JPanel centre = new JPanel(new BorderLayout());
        centre.setBackground(BG);
        centre.setBorder(BorderFactory.createEmptyBorder(0, 15, 5, 15));
        centre.add(searchBar, BorderLayout.NORTH);
        centre.add(scroll, BorderLayout.CENTER);
        main.add(centre, BorderLayout.CENTER);

        // Footer count label
        JLabel lblCount = new JLabel("", SwingConstants.CENTER);
        lblCount.setFont(new Font("SansSerif", Font.ITALIC, 11));
        lblCount.setForeground(Color.GRAY);
        lblCount.setBorder(BorderFactory.createEmptyBorder(5, 0, 8, 0));
        main.add(lblCount, BorderLayout.SOUTH);

        add(main);

        btnSearch.addActionListener(e -> filterCustomers(tfSearch.getText().trim()));
        btnAll.addActionListener(e -> { tfSearch.setText(""); populateTable(allCustomers); });
    }

    private void loadCustomers() {
        allCustomers = userDAO.getAllCustomers();
        populateTable(allCustomers);
    }

    private void filterCustomers(String keyword) {
        if (keyword.isEmpty()) {
            populateTable(allCustomers);
            return;
        }
        String kw = keyword.toLowerCase();
        List<User> filtered = new java.util.ArrayList<>();
        for (User u : allCustomers) {
            if (u.getName().toLowerCase().contains(kw) ||
                u.getEmail().toLowerCase().contains(kw)) {
                filtered.add(u);
            }
        }
        populateTable(filtered);
    }

    private void populateTable(List<User> list) {
        tableModel.setRowCount(0);
        for (User u : list) {
            tableModel.addRow(new Object[]{
                u.getUserId(), u.getName(), u.getEmail(), u.getPhone(), u.getRole()
            });
        }
        // Update count in title bar
        setTitle("View All Customers  (" + list.size() + " found)");
    }

    private void styleBtn(JButton btn, Color bg) {
        btn.setBackground(bg);
        btn.setForeground(WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
        btn.setPreferredSize(new Dimension(110, 30));
    }
}
