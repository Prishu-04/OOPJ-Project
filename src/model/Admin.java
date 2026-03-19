package model;

/**
 * Admin - Demonstrates INHERITANCE (extends User) and POLYMORPHISM (overrides getDisplayName).
 * Admin has an extra field: adminCode, and overrides getDisplayName().
 */
public class Admin extends User {

    private String adminCode;   // Additional field specific to Admin

    // -------------------------------------------------------
    // Constructors
    // -------------------------------------------------------

    public Admin() {
        super();
        this.setRole("admin");
    }

    public Admin(int userId, String name, String email, String phone, String password) {
        super(userId, name, email, phone, password, "admin");
        this.adminCode = "ADM-" + userId;
    }

    public Admin(String name, String email, String phone, String password) {
        super(name, email, phone, password, "admin");
    }

    // -------------------------------------------------------
    // Getter / Setter for adminCode
    // -------------------------------------------------------

    public String getAdminCode()                   { return adminCode; }
    public void   setAdminCode(String adminCode)   { this.adminCode = adminCode; }

    // -------------------------------------------------------
    // POLYMORPHISM - overrides User.getDisplayName()
    // -------------------------------------------------------

    @Override
    public String getDisplayName() {
        return "[Admin] " + getName();
    }

    @Override
    public String toString() {
        return "Admin{id=" + getUserId() + ", name=" + getName()
               + ", email=" + getEmail() + ", adminCode=" + adminCode + "}";
    }
}
