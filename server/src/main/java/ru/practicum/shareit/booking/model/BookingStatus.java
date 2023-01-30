package ru.practicum.shareit.booking.model;

public class BookingStatus {
    public enum Status {
        WAITING,
        APPROVED,
        REJECTED,
        CANCELED
    }
}