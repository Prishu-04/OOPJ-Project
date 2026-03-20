package model;

/**
 * Customer - Demonstrates Inheritance (extends User).
 * Overrides getDashboardTitle() → Polymorphism.
 */
public class Customer extends User {

    public Customer() {
        super();
    }

    public Customer(int userId, String name, String email,
                    String phone, String password) {
        super(userId, name, email, phone, password, "customer");
    }

    /**
     * Polymorphism: Customer-specific dashboard title.
     */
    @Override
    public String getDashboardTitle() {
        return "User Dashboard — Hotel Reservation System";
    }
}
