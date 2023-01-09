package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.service.BookingServiceImpl;

import java.util.List;

/**
 * TODO Sprint add-bookings.
 */

@Slf4j
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingServiceImpl bookingServiceImpl) {
        this.bookingService = bookingServiceImpl;
    }

    @PostMapping
    public BookingDto addBooking(@RequestHeader("X-Sharer-User-Id") int bookerId, @RequestBody Booking booking) {
        log.info("Создать запрос на аренду: {}", booking);

        booking.setBookerId(bookerId);

        return bookingService.addBooking(booking);
    }

    @PatchMapping("/{id}")
    public BookingDto approveBooking(@RequestHeader("X-Sharer-User-Id") int bookerId, @PathVariable("id") Integer bookingId, @RequestParam("approved") boolean isApprove) {
        log.info("Результат подтверждения аренды: {}", isApprove);

        return bookingService.approveBooking(bookerId, bookingId, isApprove);
    }

    @GetMapping("/{id}")
    public BookingDto getBooking(@RequestHeader("X-Sharer-User-Id") int userId, @PathVariable int id) {
        log.info("Получить информацию о бронировании UserId = {}, BookingId = {}", userId, id);

        return bookingService.getBooking(userId, id);

    }

    @GetMapping
    public List<BookingDto> getBookings(@RequestHeader("X-Sharer-User-Id") int userId, @RequestParam(defaultValue = "all") String state) {
        log.info("Поучить все заявки пользователя = {}", userId);

        return bookingService.allBookings(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingDto> getOwnerBookings(@RequestHeader("X-Sharer-User-Id") int userId, @RequestParam(defaultValue = "all") String state) {
        log.info("Поучить все заявки на предметы владельца ID = {}", userId);

        return bookingService.allOwnerBookings(userId, state);

    }
}
