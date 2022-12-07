package ru.practicum.shareit.booking.model;

import lombok.Data;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDate;

/**
 * TODO Sprint add-bookings.
 */

@Data
public class Booking {
    private Integer id;
    private LocalDate bookingStartTime;
    private LocalDate bookingEndTime;
    private Item item;
    private User booker;
    private BookingStatus.Status bookingStatus;
}


