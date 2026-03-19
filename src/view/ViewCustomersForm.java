package view;

import dao.UserDAO;
import model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * ViewCustomersForm - Admin views all registered customers.
 */
public class ViewCustomersForm extends JFrame {

    private JTable            table;
    private DefaultTableModel tableModel;
    private JButton           btnRefresh, btnClose;

    private UserDAO userDAO = new UserDAO();

    public ViewCustomersForm() {
        setTitle("All Customers - Admin View");
        setSize(640, 420);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        initUI();
        loadCustomers();
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 10, 15));

        JLabel lbl = new JLabel("Registered Customers", SwingConstants.CENTER);
        lbl.setFont(new Font("Arial", Font.BOLD, 16));
        lbl.setForeground(new Color(30, 80, 150));
        mainPanel.add(lbl, BorderLayout.NORTH);

        String[] cols = {"User ID", "Name", "Email", "Phone", "Role"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        table.setRowHeight(24);

        mainPanel.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 5));
        btnRefresh = new JButton("Refresh");
        btnClose   = new JButton("Close");
        btnRefresh.setBackground(new Color(30, 100, 180));
        btnRefresh.setForeground(Color.WHITE);
        btnRefresh.setFocusPainted(false);
        btnPanel.add(btnRefresh);
        btnPanel.add(btnClose);
        mainPanel.add(btnPanel, BorderLayout.SOUTH);

        add(mainPanel);

        btnRefresh.addActionListener(e -> loadCustomers());
        btnClose.addActionListener(e -> dispose());
    }

    private void loadCustomers() {
        tableModel.setRowCount(0);
        List<User> users = userDAO.getAll();
        for (User u : users) {
            tableModel.addRow(new Object[]{
                u.getUserId(), u.getName(), u.getEmail(), u.getPhone(), u.getRole()
            });
        }
        if (users.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "No customers registered yet.", "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
