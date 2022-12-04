package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDate;

/**
 * TODO Sprint add-bookings.
 */
public class BookingDto {
    Integer id;
    LocalDate start;
    LocalDate end;
    Item item;
    User booker;
    Status status;


    enum Status {
        WAITING,
        APPROVED,
        REJECTED,
        CANCELED
    }
}
