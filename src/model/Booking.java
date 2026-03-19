package model;

import java.sql.Date;

/**
 * Booking - Represents a room reservation made by a customer.
 */
public class Booking {

    private int    bookingId;
    private int    userId;
    private int    roomId;
    private Date   checkIn;
    private Date   checkOut;
    private double totalAmount;
    private String bookingStatus;   // Confirmed | Cancelled

    // Extra display fields (populated by JOIN queries)
    private String userName;
    private String roomNumber;
    private String roomType;

    // -------------------------------------------------------
    // Constructors
    // -------------------------------------------------------

    public Booking() {}

    public Booking(int userId, int roomId, Date checkIn, Date checkOut,
                   double totalAmount, String bookingStatus) {
        this.userId        = userId;
        this.roomId        = roomId;
        this.checkIn       = checkIn;
        this.checkOut      = checkOut;
        this.totalAmount   = totalAmount;
        this.bookingStatus = bookingStatus;
    }

    public Booking(int bookingId, int userId, int roomId, Date checkIn, Date checkOut,
                   double totalAmount, String bookingStatus) {
        this.bookingId     = bookingId;
        this.userId        = userId;
        this.roomId        = roomId;
        this.checkIn       = checkIn;
        this.checkOut      = checkOut;
        this.totalAmount   = totalAmount;
        this.bookingStatus = bookingStatus;
    }

    // -------------------------------------------------------
    // Getters and Setters
    // -------------------------------------------------------

    public int    getBookingId()                       { return bookingId; }
    public void   setBookingId(int bookingId)          { this.bookingId = bookingId; }

    public int    getUserId()                  { return userId; }
    public void   setUserId(int userId)        { this.userId = userId; }

    public int    getRoomId()                  { return roomId; }
    public void   setRoomId(int roomId)        { this.roomId = roomId; }

    public Date   getCheckIn()                     { return checkIn; }
    public void   setCheckIn(Date checkIn)         { this.checkIn = checkIn; }

    public Date   getCheckOut()                    { return checkOut; }
    public void   setCheckOut(Date checkOut)       { this.checkOut = checkOut; }

    public double getTotalAmount()                         { return totalAmount; }
    public void   setTotalAmount(double totalAmount)       { this.totalAmount = totalAmount; }

    public String getBookingStatus()                           { return bookingStatus; }
    public void   setBookingStatus(String bookingStatus)       { this.bookingStatus = bookingStatus; }

    // Display helpers (set after JOIN queries)
    public String getUserName()                    { return userName; }
    public void   setUserName(String userName)     { this.userName = userName; }

    public String getRoomNumber()                      { return roomNumber; }
    public void   setRoomNumber(String roomNumber)     { this.roomNumber = roomNumber; }

    public String getRoomType()                    { return roomType; }
    public void   setRoomType(String roomType)     { this.roomType = roomType; }

    @Override
    public String toString() {
        return "Booking{id=" + bookingId + ", userId=" + userId + ", roomId=" + roomId
               + ", checkIn=" + checkIn + ", checkOut=" + checkOut
               + ", amount=" + totalAmount + ", status=" + bookingStatus + "}";
    }
}
