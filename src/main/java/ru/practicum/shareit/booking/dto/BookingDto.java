package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */

@Data
@Builder
public class BookingDto {
    private Integer id;
    private LocalDateTime start;
    private LocalDateTime end;
    private BookingStatus.Status status;
    private BookerDto booker;
    private BookingItemDto item;

}
