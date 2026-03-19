package model;

/**
 * User - Base class demonstrating ENCAPSULATION.
 * All fields are private; access is through getters/setters.
 * Admin class will extend this (INHERITANCE).
 */
public class User {

    // Private fields — Encapsulation
    private int    userId;
    private String name;
    private String email;
    private String phone;
    private String password;
    private String role;

    // -------------------------------------------------------
    // Constructors
    // -------------------------------------------------------

    public User() {}

    public User(int userId, String name, String email, String phone, String password, String role) {
        this.userId   = userId;
        this.name     = name;
        this.email    = email;
        this.phone    = phone;
        this.password = password;
        this.role     = role;
    }

    // Constructor without id (used during registration)
    public User(String name, String email, String phone, String password, String role) {
        this.name     = name;
        this.email    = email;
        this.phone    = phone;
        this.password = password;
        this.role     = role;
    }

    // -------------------------------------------------------
    // Getters and Setters
    // -------------------------------------------------------

    public int getUserId()             { return userId; }
    public void setUserId(int userId)  { this.userId = userId; }

    public String getName()                { return name; }
    public void   setName(String name)     { this.name = name; }

    public String getEmail()               { return email; }
    public void   setEmail(String email)   { this.email = email; }

    public String getPhone()               { return phone; }
    public void   setPhone(String phone)   { this.phone = phone; }

    public String getPassword()                    { return password; }
    public void   setPassword(String password)     { this.password = password; }

    public String getRole()                { return role; }
    public void   setRole(String role)     { this.role = role; }

    // -------------------------------------------------------
    // POLYMORPHISM - method overriding example
    // -------------------------------------------------------

    /**
     * Returns a display string for this user.
     * Admin will override this to add "Admin" prefix.
     */
    public String getDisplayName() {
        return name + " (" + role + ")";
    }

    @Override
    public String toString() {
        return "User{id=" + userId + ", name=" + name + ", email=" + email + ", role=" + role + "}";
    }
}
