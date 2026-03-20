package model;

/**
 * Room - Represents a hotel room.
 * Encapsulation via private fields + getters/setters.
 */
public class Room {

    private int    roomId;
    private String roomNumber;
    private String roomType;
    private double price;
    private String status;

    public Room() {}

    public Room(int roomId, String roomNumber, String roomType,
                double price, String status) {
        this.roomId     = roomId;
        this.roomNumber = roomNumber;
        this.roomType   = roomType;
        this.price      = price;
        this.status     = status;
    }

    // ──────────── Getters & Setters ────────────

    public int getRoomId()                  { return roomId; }
    public void setRoomId(int roomId)       { this.roomId = roomId; }

    public String getRoomNumber()               { return roomNumber; }
    public void setRoomNumber(String roomNumber){ this.roomNumber = roomNumber; }

    public String getRoomType()               { return roomType; }
    public void setRoomType(String roomType)  { this.roomType = roomType; }

    public double getPrice()            { return price; }
    public void setPrice(double price)  { this.price = price; }

    public String getStatus()             { return status; }
    public void setStatus(String status)  { this.status = status; }

    @Override
    public String toString() {
        return "Room{" + roomNumber + ", " + roomType + ", ₹" + price + ", " + status + "}";
    }
}
