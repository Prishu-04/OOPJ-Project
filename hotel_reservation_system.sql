-- ============================================================
-- Hotel Reservation System - MySQL Database Schema
-- ============================================================

CREATE DATABASE IF NOT EXISTS hotel_reservation_system;
USE hotel_reservation_system;

-- ============================================================
-- TABLE: users
-- ============================================================
CREATE TABLE IF NOT EXISTS users (
    user_id    INT          PRIMARY KEY AUTO_INCREMENT,
    name       VARCHAR(100) NOT NULL,
    email      VARCHAR(100) NOT NULL UNIQUE,
    phone      VARCHAR(20)  NOT NULL,
    password   VARCHAR(100) NOT NULL,
    role       VARCHAR(20)  NOT NULL DEFAULT 'customer'
);

-- ============================================================
-- TABLE: rooms
-- ============================================================
CREATE TABLE IF NOT EXISTS rooms (
    room_id     INT           PRIMARY KEY AUTO_INCREMENT,
    room_number VARCHAR(20)   NOT NULL UNIQUE,
    room_type   VARCHAR(50)   NOT NULL,
    price       DOUBLE        NOT NULL,
    status      VARCHAR(20)   NOT NULL DEFAULT 'Available'
);

-- ============================================================
-- TABLE: bookings
-- ============================================================
CREATE TABLE IF NOT EXISTS bookings (
    booking_id     INT          PRIMARY KEY AUTO_INCREMENT,
    user_id        INT          NOT NULL,
    room_id        INT          NOT NULL,
    check_in       DATE         NOT NULL,
    check_out      DATE         NOT NULL,
    total_amount   DOUBLE       NOT NULL,
    booking_status VARCHAR(20)  NOT NULL DEFAULT 'Confirmed',
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (room_id) REFERENCES rooms(room_id)
);

-- ============================================================
-- TABLE: payments
-- ============================================================
CREATE TABLE IF NOT EXISTS payments (
    payment_id     INT         PRIMARY KEY AUTO_INCREMENT,
    booking_id     INT         NOT NULL,
    amount         DOUBLE      NOT NULL,
    payment_date   DATE        NOT NULL,
    payment_method VARCHAR(50) NOT NULL,
    payment_status VARCHAR(20) NOT NULL DEFAULT 'Paid',
    FOREIGN KEY (booking_id) REFERENCES bookings(booking_id)
);

-- ============================================================
-- DEFAULT ADMIN ACCOUNT
-- Email: admin@hotel.com | Password: admin123
-- ============================================================
INSERT INTO users (name, email, phone, password, role)
VALUES ('Admin', 'admin@hotel.com', '9999999999', 'admin123', 'admin');

-- ============================================================
-- SAMPLE ROOMS
-- ============================================================
INSERT INTO rooms (room_number, room_type, price, status) VALUES
('101', 'Single',  1500.00, 'Available'),
('102', 'Single',  1500.00, 'Available'),
('201', 'Double',  2500.00, 'Available'),
('202', 'Double',  2500.00, 'Available'),
('301', 'Suite',   5000.00, 'Available'),
('302', 'Suite',   5000.00, 'Available'),
('401', 'Deluxe',  3500.00, 'Available'),
('402', 'Deluxe',  3500.00, 'Available');
