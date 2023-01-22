package ru.practicum.shareit.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.exception.IncorrectParamInRequestException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.exception.UserNotOwnerException;
import ru.practicum.shareit.item.dto.ItemBookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {

    BookingServiceImpl bookingServiceImpl;

    @Mock
    BookingRepository bookingRepository;

    @Mock
    UserServiceImpl userServiceImpl;

    @Mock
    ItemServiceImpl itemServiceImpl;

    private Booking testBooking = Booking.builder()
            .id(4)
            .start(LocalDateTime.now())
            .end(LocalDateTime.now())
            .itemId(1)
            .bookerId(21)
            .status(BookingStatus.Status.APPROVED)
            .build();

    private ItemDto itemDto = ItemDto.builder()
            .id(1)
            .name("A")
            .owner(2)
            .requestId(5)
            .description("AA")
            .available(true)
            .lastBooking(ItemBookingDto.builder()
                    .id(11)
                    .bookerId(22)
                    .build()
            )
            .nextBooking(
                    ItemBookingDto.builder()
                            .id(33)
                            .bookerId(44)
                            .build()
            )
            .build();

    private UserDto userDto = UserDto.builder()
            .id(1)
            .name("A")
            .email("a@b.ru")
            .build();

    @BeforeEach
    void setUp() {
        bookingServiceImpl = new BookingServiceImpl(bookingRepository, itemServiceImpl, userServiceImpl);
    }

    @Test
    void getBookingWithoutUser() {
        when(bookingRepository.countBookingsById(anyInt()))
                .thenReturn(0L);

        Throwable exception = assertThrows(
                UserNotFoundException.class,
                () -> bookingServiceImpl.getBooking(1, 1)
        );
        assertEquals("Пользователя с таким ID = 1 не существует!", exception.getMessage());

    }

    @Test
    void getBookingWhenUserNotOwnerOrBooker() {
        when(bookingRepository.countBookingsById(anyInt()))
                .thenReturn(3L);

        when(bookingRepository.getById(anyInt()))
                .thenReturn(testBooking);

        when(itemServiceImpl.getItem(anyInt(), any()))
                .thenReturn(itemDto);

        Throwable exception = assertThrows(
                UserNotFoundException.class,
                () -> bookingServiceImpl.getBooking(1, 4)
        );
        assertEquals("Доступ запрещен!", exception.getMessage());

    }

    @Test
    void getBookingUserIsOwner() {
        when(bookingRepository.countBookingsById(anyInt()))
                .thenReturn(3L);

        when(bookingRepository.getById(anyInt()))
                .thenReturn(testBooking);

        when(itemServiceImpl.getItem(anyInt(), any()))
                .thenReturn(itemDto);

        BookingDto booking = bookingServiceImpl.getBooking(2, 4);

        assertNotNull(booking);
        assertEquals(4, booking.getId());
    }

    @Test
    void getBookingUserIsBooker() {
        when(bookingRepository.countBookingsById(anyInt()))
                .thenReturn(3L);

        when(bookingRepository.getById(anyInt()))
                .thenReturn(testBooking);

        when(itemServiceImpl.getItem(anyInt(), any()))
                .thenReturn(itemDto);

        BookingDto booking = bookingServiceImpl.getBooking(21, 4);

        assertNotNull(booking);
        assertEquals(4, booking.getId());
    }

//    @Test TODO
    void addBooking() {

    }

    @Test
    void allBookingsWithoutUser() {
        when(userServiceImpl.getUser(anyInt()))
                .thenReturn(null);

        Throwable exception = assertThrows(
                UserNotFoundException.class,
                () -> bookingServiceImpl.allBookings(1, "all", 0, 1)
        );

        assertEquals("Пользователя с таким ID = 1 не существует!", exception.getMessage());

    }

    @Test
    void allBookingsWithCurrentState() {
        String state = "cUrRent";

        when(userServiceImpl.getUser(anyInt()))
                .thenReturn(userDto);

        Booking bookingOne = Booking.builder()
                .id(1)
                .itemId(2)
                .status(BookingStatus.Status.APPROVED)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now())
                .bookerId(7)
                .build();

        Booking bookingTwo = Booking.builder()
                .id(3)
                .itemId(4)
                .status(BookingStatus.Status.APPROVED)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now())
                .bookerId(8)
                .build();

        Page<Booking> bookings = new PageImpl<>(List.of(bookingOne, bookingTwo));

        when(bookingRepository.getByBookerIdAndEndIsAfterAndStartIsBeforeOrderByStartDesc(anyInt(), any(), any(), any(Pageable.class)))
                .thenReturn(bookings);

        when(itemServiceImpl.getItem(anyInt(), any()))
                .thenReturn(itemDto);

        List<BookingDto> current = bookingServiceImpl.allBookings(1, state, 0, 1);

        assertEquals(2, current.size());
    }

    @Test
    void allBookingsWithPastState() {
        String state = "paSt";

        when(userServiceImpl.getUser(anyInt()))
                .thenReturn(userDto);

        Booking bookingOne = Booking.builder()
                .id(1)
                .itemId(2)
                .status(BookingStatus.Status.APPROVED)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now())
                .bookerId(7)
                .build();

        Booking bookingTwo = Booking.builder()
                .id(3)
                .itemId(4)
                .status(BookingStatus.Status.APPROVED)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now())
                .bookerId(8)
                .build();

        List<Booking> bookings = List.of(bookingOne, bookingTwo);

        when(bookingRepository.findByEndIsBefore(any(), any()))
                .thenReturn(bookings);

        when(itemServiceImpl.getItem(anyInt(), any()))
                .thenReturn(itemDto);

        List<BookingDto> current = bookingServiceImpl.allBookings(1, state, 0, 1);

        assertEquals(2, current.size());
    }

    @Test
    void allBookingsWithFutureState() {
        String state = "FutuRe";

        when(userServiceImpl.getUser(anyInt()))
                .thenReturn(userDto);

        Booking bookingOne = Booking.builder()
                .id(1)
                .itemId(2)
                .status(BookingStatus.Status.APPROVED)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now())
                .bookerId(7)
                .build();

        Booking bookingTwo = Booking.builder()
                .id(3)
                .itemId(4)
                .status(BookingStatus.Status.APPROVED)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now())
                .bookerId(8)
                .build();

        List<Booking> bookings = List.of(bookingOne, bookingTwo);

        when(bookingRepository.findByBookerIdAndStartIsAfter(anyInt(), any(), any()))
                .thenReturn(bookings);

        when(itemServiceImpl.getItem(anyInt(), any()))
                .thenReturn(itemDto);

        List<BookingDto> current = bookingServiceImpl.allBookings(1, state, 0, 1);

        assertEquals(2, current.size());
    }

    @Test
    void allBookingsWithWaitingState() {
        String state = "waItiNg";

        when(userServiceImpl.getUser(anyInt()))
                .thenReturn(userDto);

        Booking bookingOne = Booking.builder()
                .id(1)
                .itemId(2)
                .status(BookingStatus.Status.APPROVED)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now())
                .bookerId(7)
                .build();

        Booking bookingTwo = Booking.builder()
                .id(3)
                .itemId(4)
                .status(BookingStatus.Status.APPROVED)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now())
                .bookerId(8)
                .build();

        List<Booking> bookings = List.of(bookingOne, bookingTwo);

        when(bookingRepository.findByStatusAndBookerIdOrderByStartDesc(any(), anyInt()))
                .thenReturn(bookings);

        when(itemServiceImpl.getItem(anyInt(), any()))
                .thenReturn(itemDto);

        List<BookingDto> current = bookingServiceImpl.allBookings(1, state, 0, 1);

        assertEquals(2, current.size());
    }

    @Test
    void allBookingsWithRejectedState() {
        String state = "reJEcted";

        when(userServiceImpl.getUser(anyInt()))
                .thenReturn(userDto);

        Booking bookingOne = Booking.builder()
                .id(1)
                .itemId(2)
                .status(BookingStatus.Status.APPROVED)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now())
                .bookerId(7)
                .build();

        Booking bookingTwo = Booking.builder()
                .id(3)
                .itemId(4)
                .status(BookingStatus.Status.APPROVED)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now())
                .bookerId(8)
                .build();

        List<Booking> bookings = List.of(bookingOne, bookingTwo);

        when(bookingRepository.findByStatusAndBookerIdOrderByStartDesc(any(), anyInt()))
                .thenReturn(bookings);

        when(itemServiceImpl.getItem(anyInt(), any()))
                .thenReturn(itemDto);

        List<BookingDto> current = bookingServiceImpl.allBookings(1, state, 0, 1);

        assertEquals(2, current.size());
    }

    @Test
    void allBookingsWithoutState() {
        String state = "";

        when(userServiceImpl.getUser(anyInt()))
                .thenReturn(userDto);

        Throwable exception = assertThrows(
                IncorrectParamInRequestException.class,
                () -> bookingServiceImpl.allBookings(1, state, 0, 1)
        );

        assertEquals("Unknown state: UNSUPPORTED_STATUS", exception.getMessage());

    }

    @Test
    void approveBookingWhenUserIsBooker() {

        when(bookingRepository.getById(anyInt()))
                .thenReturn(testBooking);

        Throwable exception = assertThrows(
                UserNotFoundException.class,
                () -> bookingServiceImpl.approveBooking(21, 2, true)
        );

        assertEquals("You're a booker, not owner!", exception.getMessage());

    }

    @Test
    void approveBookingWhenUserIsNotOwner() {

        when(bookingRepository.getById(anyInt()))
                .thenReturn(testBooking);

        when(itemServiceImpl.getItem(anyInt(), any()))
                .thenReturn(itemDto);

        Throwable exception = assertThrows(
                UserNotOwnerException.class,
                () -> bookingServiceImpl.approveBooking(11, 2, true)
        );

        assertEquals("Пользователь с ID = 11 не является владельцем продукта", exception.getMessage());

    }

    @Test
    void approveBookingWhenUBookingStatusIsApproved() {

        when(bookingRepository.getById(anyInt()))
                .thenReturn(testBooking);

        when(itemServiceImpl.getItem(anyInt(), any()))
                .thenReturn(itemDto);

        Throwable exception = assertThrows(
                IncorrectParamInRequestException.class,
                () -> bookingServiceImpl.approveBooking(2, 2, true)
        );

        assertEquals("Аренда уже подтверждена", exception.getMessage());

    }
}
