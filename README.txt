===========================================================
  HOTEL RESERVATION SYSTEM — SEM 4 OOPJ Project
  Java | Swing | JDBC | MySQL
===========================================================

-----------------------------------------------------------
1. PROJECT OVERVIEW
-----------------------------------------------------------
A desktop-based Hotel Reservation System built with Java
Swing and MySQL. Supports two roles:
  - Admin  : manages rooms and views all bookings/customers
  - Customer: registers, books rooms, cancels, pays

-----------------------------------------------------------
2. PACKAGE / FOLDER STRUCTURE
-----------------------------------------------------------

HotelReservationSystem/
 ├── hotel_reservation_system.sql       <- Run this in MySQL first
 ├── README.txt
 └── src/
      ├── db/
      │    └── DBConnection.java
      ├── model/
      │    ├── User.java
      │    ├── Admin.java
      │    ├── Room.java
      │    ├── Booking.java
      │    └── Payment.java
      ├── dao/
      │    ├── IDao.java               <- Interface (Abstraction)
      │    ├── UserDAO.java
      │    ├── RoomDAO.java
      │    ├── BookingDAO.java
      │    └── PaymentDAO.java
      ├── view/
      │    ├── LoginForm.java
      │    ├── RegisterForm.java
      │    ├── AdminDashboard.java
      │    ├── UserDashboard.java
      │    ├── AddRoomForm.java
      │    ├── ManageRoomsForm.java
      │    ├── ViewRoomsForm.java
      │    ├── BookingForm.java
      │    ├── MyBookingsForm.java
      │    ├── CancelBookingForm.java
      │    ├── PaymentForm.java
      │    ├── ViewAllBookingsForm.java
      │    └── ViewCustomersForm.java
      └── main/
           └── Main.java

-----------------------------------------------------------
3. DEPENDENCY — MySQL Connector/J
-----------------------------------------------------------
Download: https://dev.mysql.com/downloads/connector/j/
  → Choose "Platform Independent" → Download ZIP
  → Extract the JAR file, e.g. mysql-connector-j-8.x.x.jar

Add to your project in your IDE:
  IntelliJ IDEA:
    File → Project Structure → Libraries → + → Java
    → Select the .jar file → Apply

  Eclipse:
    Right-click project → Build Path → Add External Archives
    → Select the .jar file

  NetBeans:
    Right-click project → Properties → Libraries → Add JAR/Folder
    → Select the .jar file

-----------------------------------------------------------
4. DATABASE SETUP
-----------------------------------------------------------
Step 1: Start MySQL (XAMPP, MySQL Workbench, or command line)

Step 2: Run the SQL script:
  mysql -u root -p < hotel_reservation_system.sql
  OR open hotel_reservation_system.sql in MySQL Workbench
  and execute it.

Step 3: Verify tables exist:
  USE hotel_reservation_system;
  SHOW TABLES;
  -- Should show: users, rooms, bookings, payments

Step 4: Edit src/db/DBConnection.java if needed:
  private static final String URL      = "jdbc:mysql://localhost:3306/hotel_reservation_system";
  private static final String USERNAME = "root";
  private static final String PASSWORD = "";   // <-- your MySQL password

-----------------------------------------------------------
5. HOW TO RUN
-----------------------------------------------------------
In any IDE (IntelliJ / Eclipse / NetBeans):

  1. Create a new Java Project
  2. Copy all source files into the src/ directory,
     maintaining the package structure shown above
  3. Add mysql-connector-j.jar to the build path
  4. Run  src/main/Main.java

Command line (if javac is configured):
  cd HotelReservationSystem
  javac -cp .;mysql-connector-j.jar -d out src/**/*.java
  java  -cp out;mysql-connector-j.jar main.Main

-----------------------------------------------------------
6. SAMPLE LOGIN CREDENTIALS
-----------------------------------------------------------

ADMIN LOGIN:
  Email   : admin@hotel.com
  Password: admin123

CUSTOMER REGISTRATION FLOW:
  1. Open app → Click "Register"
  2. Fill in: Name, Email, Phone, Password, Confirm Password
  3. Click Register → login with those credentials

-----------------------------------------------------------
7. SAMPLE BOOKING FLOW
-----------------------------------------------------------
  1. Login as customer
  2. Dashboard → "Book a Room"
  3. Select room from dropdown
  4. Enter Check-In: 2025-06-01
  5. Enter Check-Out: 2025-06-05
  6. Click "Calculate Total" → Total auto-fills
  7. Click "Confirm Booking" → Receipt shown
  8. Dashboard → "My Bookings" → See the booking
  9. Dashboard → "Make Payment" → Pay via UPI/Cash/Card

-----------------------------------------------------------
8. OOP CONCEPTS USED
-----------------------------------------------------------

ENCAPSULATION:
  - All model classes (User, Room, Booking, Payment) have
    private fields and public getters/setters.
  - Example: User.java — private String name; + getName()/setName()

INHERITANCE:
  - Admin extends User (src/model/Admin.java)
  - Admin inherits name, email, phone, password, role from User
  - Admin adds adminCode field

POLYMORPHISM:
  - Method Overriding: Admin.getDisplayName() overrides User.getDisplayName()
    User returns:  "John (customer)"
    Admin returns: "[Admin] John"
  - Method Overloading: Multiple constructors in User, Room, Booking, Payment

ABSTRACTION:
  - IDao<T, ID> interface in dao/IDao.java defines the contract:
    add(), getById(), getAll(), update(), delete()
  - UserDAO, RoomDAO, BookingDAO, PaymentDAO all implement IDao
  - The caller only needs to know IDao, not the SQL details

CLASSES & OBJECTS:
  - Model classes: User, Admin, Room, Booking, Payment
  - DAO classes: UserDAO, RoomDAO, BookingDAO, PaymentDAO
  - View classes: Each form is a class extending JFrame

-----------------------------------------------------------
9. COMMON ERRORS AND FIXES
-----------------------------------------------------------

ERROR: "No suitable driver found"
  FIX: Add mysql-connector-j.jar to the classpath.
       Check Class.forName("com.mysql.cj.jdbc.Driver") in DBConnection.

ERROR: "Access denied for user 'root'@'localhost'"
  FIX: Update USERNAME and PASSWORD in DBConnection.java.

ERROR: "Unknown database 'hotel_reservation_system'"
  FIX: Run hotel_reservation_system.sql first in MySQL.

ERROR: "Communications link failure"
  FIX: Make sure MySQL server is running (start XAMPP or MySQL service).

ERROR: "Table 'users' doesn't exist"
  FIX: Run the full SQL script including CREATE TABLE statements.

ERROR: Compilation error — "package view does not exist"
  FIX: Make sure the package declaration at the top of each file
       matches the folder it is in:
       package view;   <- in the view/ folder
       package model;  <- in the model/ folder

-----------------------------------------------------------
10. NOTES FOR VIVA / SUBMISSION
-----------------------------------------------------------
  - The project uses PreparedStatement everywhere (no string SQL injection risk)
  - IDao interface demonstrates abstraction and dependency inversion
  - Admin extends User with role-specific behaviour
  - All forms are separate JFrame classes for clean separation
  - DBConnection uses a static factory method (similar to Singleton pattern)
  - The project is runnable entirely from Main.java
