package model;

/**
 * Payment - Represents a payment record.
 */
public class Payment {

    private int    paymentId;
    private int    bookingId;
    private double amount;
    private String paymentDate;
    private String paymentMethod;
    private String paymentStatus;

    public Payment() {}

    public Payment(int paymentId, int bookingId, double amount,
                   String paymentDate, String paymentMethod, String paymentStatus) {
        this.paymentId     = paymentId;
        this.bookingId     = bookingId;
        this.amount        = amount;
        this.paymentDate   = paymentDate;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
    }

    // ──────────── Getters & Setters ────────────

    public int getPaymentId()                   { return paymentId; }
    public void setPaymentId(int paymentId)     { this.paymentId = paymentId; }

    public int getBookingId()                   { return bookingId; }
    public void setBookingId(int bookingId)     { this.bookingId = bookingId; }

    public double getAmount()               { return amount; }
    public void setAmount(double amount)    { this.amount = amount; }

    public String getPaymentDate()                  { return paymentDate; }
    public void setPaymentDate(String paymentDate)  { this.paymentDate = paymentDate; }

    public String getPaymentMethod()                    { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod)  { this.paymentMethod = paymentMethod; }

    public String getPaymentStatus()                    { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus)  { this.paymentStatus = paymentStatus; }
}
