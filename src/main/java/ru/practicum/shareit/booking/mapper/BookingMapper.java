package ru.practicum.shareit.booking.mapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.booking.dto.BookerDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemDto;

@Data
@AllArgsConstructor
public class BookingMapper {

    public static BookingDto toBookingDto(Booking booking, ItemDto itemDto) {
        return BookingDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .status(booking.getStatus())
                .booker(
                        BookerDto.builder()
                                .id(booking.getBookerId())
                                .build()
                )
                .item(
                        BookingItemDto.builder()
                                .id(itemDto.getId())
                                .name(itemDto.getName())
                                .build()
                )
                .build();
    }

    public static Booking toBooking(BookingDto bookingDto) {
        return Booking.builder()
                .id(bookingDto.getId())
                .start(bookingDto.getStart())
                .end(bookingDto.getEnd())
                .itemId(bookingDto.getItem().getId())
                .bookerId(bookingDto.getBooker().getId())
                .status(bookingDto.getStatus())
                .build();
    }

}
