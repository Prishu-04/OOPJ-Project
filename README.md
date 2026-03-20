# 🏨 Hotel Reservation System
### SEM 4 — Object-Oriented Programming with Java (OOPJ) Project

---

## 📁 Project Structure

```
HotelReservationSystem/
├── pom.xml                    ← Maven build file (includes SQLite dependency)
├── compile_run.bat            ← Windows compile + run script (non-Maven)
├── compile_run.sh             ← Linux/macOS compile + run script (non-Maven)
├── libs/                      ← Place sqlite-jdbc JAR here (non-Maven method)
└── src/
    ├── db/
    │   └── DBConnection.java  ← SQLite connection + auto table creation
    ├── model/
    │   ├── User.java          ← Abstract base class (Abstraction)
    │   ├── Admin.java         ← Extends User (Inheritance + Polymorphism)
    │   ├── Customer.java      ← Extends User (Inheritance + Polymorphism)
    │   ├── Room.java          ← Room entity (Encapsulation)
    │   ├── Booking.java       ← Booking entity
    │   └── Payment.java       ← Payment entity
    ├── dao/
    │   ├── UserDAO.java       ← DB ops for users (login, register)
    │   ├── RoomDAO.java       ← DB ops for rooms (CRUD)
    │   ├── BookingDAO.java    ← DB ops for bookings
    │   └── PaymentDAO.java    ← DB ops for payments
    ├── view/
    │   ├── LoginForm.java
    │   ├── RegisterForm.java
    │   ├── AdminDashboard.java
    │   ├── UserDashboard.java
    │   ├── AddRoomForm.java
    │   ├── ManageRoomsForm.java
    │   ├── ViewRoomsForm.java
    │   ├── BookingForm.java
    │   ├── MyBookingsForm.java
    │   ├── CancelBookingForm.java
    │   ├── PaymentForm.java
    │   └── ViewCustomersForm.java
    └── main/
        └── Main.java          ← Entry point
```

---

## ⚙️ Dependencies

### Option A — Maven (Recommended)
The `pom.xml` automatically downloads the SQLite driver.
No manual JAR needed.

### Option B — Manual JAR
1. Download `sqlite-jdbc-3.45.1.0.jar` from:
   https://github.com/xerial/sqlite-jdbc/releases
2. Create a `libs/` folder inside the project root.
3. Place the JAR inside `libs/`.
4. Use `compile_run.bat` (Windows) or `compile_run.sh` (Linux/Mac).

---

## 🚀 How to Run

### Method 1 — IntelliJ IDEA (Recommended)
1. Open IntelliJ → File → Open → select the `HotelReservationSystem` folder.
2. If using Maven: IntelliJ auto-detects `pom.xml` and downloads the JAR.
3. If NOT using Maven: File → Project Structure → Modules → Dependencies
   → + → JARs → select `libs/sqlite-jdbc-3.45.1.0.jar`.
4. Run `src/main/Main.java`.

### Method 2 — Eclipse
1. File → Import → Existing Projects into Workspace.
2. Right-click project → Build Path → Add External Archives
   → select `sqlite-jdbc-3.45.1.0.jar`.
3. Run `Main.java` as Java Application.

### Method 3 — Maven Command Line
```bash
cd HotelReservationSystem
mvn clean package
java -jar target/HotelReservationSystem-1.0.jar
```

### Method 4 — Manual Compile (Windows)
```
compile_run.bat
```

### Method 5 — Manual Compile (Linux/Mac)
```bash
chmod +x compile_run.sh
./compile_run.sh
```

---

## 🔐 Sample Login Credentials

| Role     | Email              | Password  |
|----------|--------------------|-----------|
| Admin    | admin@hotel.com    | admin123  |
| Customer | Register new user  | any ≥ 6 chars |

---

## 🗄️ Database

- File: `hotel.db` (auto-created in project root on first run)
- Engine: SQLite
- Driver: `org.xerial:sqlite-jdbc`

### Tables Created Automatically:
| Table    | Description              |
|----------|--------------------------|
| users    | Admin & customer accounts|
| rooms    | Hotel room details       |
| bookings | Room reservations        |
| payments | Payment records          |

---

## 🧩 OOP Concepts Demonstrated

| Concept         | Where Used                                                  |
|-----------------|-------------------------------------------------------------|
| **Encapsulation** | All model classes use private fields + getters/setters    |
| **Inheritance**   | `Admin extends User`, `Customer extends User`             |
| **Abstraction**   | `User` is abstract with `getDashboardTitle()` abstract    |
| **Polymorphism**  | `Admin` and `Customer` override `getDashboardTitle()`; used in `LoginForm` to redirect to correct dashboard |

---

## ✅ Features Summary

### 👤 Customer
- Register with validation (duplicate email check, phone format, password match)
- Login → redirected to User Dashboard
- View available rooms with search/filter
- Book a room (date validation, total calculation, room status update)
- View own bookings
- Cancel booking (room status restored to Available)
- Payment form with receipt

### 🔑 Admin
- Login → redirected to Admin Dashboard
- Add / Edit / Delete rooms
- Toggle room availability
- View all bookings
- View all registered customers (with search)
- View payments
- Logout

---

## 📝 Notes for Viva / Submission

- All SQL uses `PreparedStatement` (no raw string concatenation).
- Database file `hotel.db` is created automatically — no manual SQL setup needed.
- Dates stored as TEXT in `yyyy-MM-dd` format (SQLite standard).
- The project follows a **3-tier architecture**: Model → DAO → View.
- `DBConnection` uses a singleton connection pattern.
