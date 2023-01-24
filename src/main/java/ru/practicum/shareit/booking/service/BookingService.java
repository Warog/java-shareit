package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public interface BookingService {
    BookingDto getBooking(int userid, int id);

    BookingDto addBooking(Booking booking);

    List<BookingDto> allBookings(int userid, String state);

    List<BookingDto> allOwnerBookings(int userId, String state);

    BookingDto approveBooking(int userId, int bookingId, boolean isApproved);

}
