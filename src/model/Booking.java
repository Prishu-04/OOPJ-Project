package model;

/**
 * Booking - Represents a room reservation.
 */
public class Booking {

    private int    bookingId;
    private int    userId;
    private int    roomId;
    private String checkIn;
    private String checkOut;
    private double totalAmount;
    private String bookingStatus;

    public Booking() {}

    public Booking(int bookingId, int userId, int roomId,
                   String checkIn, String checkOut,
                   double totalAmount, String bookingStatus) {
        this.bookingId     = bookingId;
        this.userId        = userId;
        this.roomId        = roomId;
        this.checkIn       = checkIn;
        this.checkOut      = checkOut;
        this.totalAmount   = totalAmount;
        this.bookingStatus = bookingStatus;
    }

    // ──────────── Getters & Setters ────────────

    public int getBookingId()                     { return bookingId; }
    public void setBookingId(int bookingId)       { this.bookingId = bookingId; }

    public int getUserId()                  { return userId; }
    public void setUserId(int userId)       { this.userId = userId; }

    public int getRoomId()                  { return roomId; }
    public void setRoomId(int roomId)       { this.roomId = roomId; }

    public String getCheckIn()              { return checkIn; }
    public void setCheckIn(String checkIn)  { this.checkIn = checkIn; }

    public String getCheckOut()               { return checkOut; }
    public void setCheckOut(String checkOut)  { this.checkOut = checkOut; }

    public double getTotalAmount()                { return totalAmount; }
    public void setTotalAmount(double totalAmount){ this.totalAmount = totalAmount; }

    public String getBookingStatus()                  { return bookingStatus; }
    public void setBookingStatus(String bookingStatus){ this.bookingStatus = bookingStatus; }

    @Override
    public String toString() {
        return "Booking{id=" + bookingId + ", userId=" + userId +
               ", roomId=" + roomId + ", status=" + bookingStatus + "}";
    }
}
