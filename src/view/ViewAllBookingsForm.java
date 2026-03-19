package view;

import dao.BookingDAO;
import model.Booking;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * ViewAllBookingsForm - Admin can view all bookings from all customers.
 */
public class ViewAllBookingsForm extends JFrame {

    private JTable            table;
    private DefaultTableModel tableModel;
    private JButton           btnRefresh, btnClose;

    private BookingDAO bookingDAO = new BookingDAO();

    public ViewAllBookingsForm() {
        setTitle("All Bookings - Admin View");
        setSize(820, 440);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        initUI();
        loadAllBookings();
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 10, 15));

        JLabel lbl = new JLabel("All Bookings", SwingConstants.CENTER);
        lbl.setFont(new Font("Arial", Font.BOLD, 16));
        lbl.setForeground(new Color(30, 80, 150));
        mainPanel.add(lbl, BorderLayout.NORTH);

        String[] cols = {"Booking ID", "Customer", "Room No", "Type",
                         "Check-In", "Check-Out", "Total (₹)", "Status"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        table.setRowHeight(24);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

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

        btnRefresh.addActionListener(e -> loadAllBookings());
        btnClose.addActionListener(e -> dispose());
    }

    private void loadAllBookings() {
        tableModel.setRowCount(0);
        List<Booking> bookings = bookingDAO.getAll();
        for (Booking b : bookings) {
            tableModel.addRow(new Object[]{
                b.getBookingId(),
                b.getUserName(),
                b.getRoomNumber(),
                b.getRoomType(),
                b.getCheckIn(),
                b.getCheckOut(),
                String.format("%.2f", b.getTotalAmount()),
                b.getBookingStatus()
            });
        }
    }
}
