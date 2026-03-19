package model;

import java.sql.Date;

/**
 * Payment - Represents a payment made for a booking.
 */
public class Payment {

    private int    paymentId;
    private int    bookingId;
    private double amount;
    private Date   paymentDate;
    private String paymentMethod;   // Cash | Card | UPI | Net Banking
    private String paymentStatus;   // Paid | Pending | Refunded

    // -------------------------------------------------------
    // Constructors
    // -------------------------------------------------------

    public Payment() {}

    public Payment(int bookingId, double amount, Date paymentDate,
                   String paymentMethod, String paymentStatus) {
        this.bookingId     = bookingId;
        this.amount        = amount;
        this.paymentDate   = paymentDate;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
    }

    public Payment(int paymentId, int bookingId, double amount, Date paymentDate,
                   String paymentMethod, String paymentStatus) {
        this.paymentId     = paymentId;
        this.bookingId     = bookingId;
        this.amount        = amount;
        this.paymentDate   = paymentDate;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
    }

    // -------------------------------------------------------
    // Getters and Setters
    // -------------------------------------------------------

    public int    getPaymentId()                       { return paymentId; }
    public void   setPaymentId(int paymentId)          { this.paymentId = paymentId; }

    public int    getBookingId()                       { return bookingId; }
    public void   setBookingId(int bookingId)          { this.bookingId = bookingId; }

    public double getAmount()                  { return amount; }
    public void   setAmount(double amount)     { this.amount = amount; }

    public Date   getPaymentDate()                     { return paymentDate; }
    public void   setPaymentDate(Date paymentDate)     { this.paymentDate = paymentDate; }

    public String getPaymentMethod()                           { return paymentMethod; }
    public void   setPaymentMethod(String paymentMethod)       { this.paymentMethod = paymentMethod; }

    public String getPaymentStatus()                           { return paymentStatus; }
    public void   setPaymentStatus(String paymentStatus)       { this.paymentStatus = paymentStatus; }

    @Override
    public String toString() {
        return "Payment{id=" + paymentId + ", bookingId=" + bookingId
               + ", amount=" + amount + ", method=" + paymentMethod
               + ", status=" + paymentStatus + "}";
    }
}
