package model;

/**
 * Admin - Demonstrates Inheritance (extends User).
 * Overrides getDashboardTitle() → Polymorphism.
 */
public class Admin extends User {

    public Admin() {
        super();
    }

    public Admin(int userId, String name, String email,
                 String phone, String password) {
        super(userId, name, email, phone, password, "admin");
    }

    /**
     * Polymorphism: Admin-specific dashboard title.
     */
    @Override
    public String getDashboardTitle() {
        return "Admin Dashboard — Hotel Management System";
    }
}
