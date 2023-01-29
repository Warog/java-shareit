package ru.practicum.shareit.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.exception.IncorrectParamInRequestException;
import ru.practicum.shareit.exception.ItemNotAvailableException;
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
            .start(LocalDateTime.now().plusDays(3))
            .end(LocalDateTime.now().plusDays(10))
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
    void getBooking_withoutUser() {
        when(bookingRepository.countBookingsById(anyInt()))
                .thenReturn(0L);

        Throwable exception = assertThrows(
                UserNotFoundException.class,
                () -> bookingServiceImpl.getBooking(1, 1)
        );
        assertEquals("Пользователя с таким ID = 1 не существует!", exception.getMessage());

    }

    @Test
    void getBooking_whenUserNotOwnerOrBooker() {
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
    void getBooking_userIsOwner() {
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
    void getBooking_userIsBooker() {
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

    @Test
    void addBooking_withoutUser() {
        ItemDto itemDtoTest = ItemDto.builder()
                .id(1)
                .name("testItem")
                .owner(2)
                .build();

        when(itemServiceImpl.getItem(anyInt(), any()))
                .thenReturn(itemDtoTest);
        when(userServiceImpl.getUser(anyInt()))
                .thenReturn(null);

        Throwable exception = assertThrows(
                UserNotFoundException.class,
                () -> bookingServiceImpl.addBooking(testBooking)
        );

        assertEquals("Пользователя с таким ID = 21 не существует!", exception.getMessage());
    }

    @Test
    void addBooking_whenItemNotAvailable() {
        ItemDto itemDtoTest = ItemDto.builder()
                .id(1)
                .name("testItem")
                .owner(2)
                .available(false)
                .build();

        when(itemServiceImpl.getItem(anyInt(), any()))
                .thenReturn(itemDtoTest);
        when(userServiceImpl.getUser(anyInt()))
                .thenReturn(new UserDto());

        Throwable exception = assertThrows(
                ItemNotAvailableException.class,
                () -> bookingServiceImpl.addBooking(testBooking)
        );

        assertEquals("Данный предмет недоступен!", exception.getMessage());
    }

    @Test
    void addBooking_startBeforeCurrentTime() {
        ItemDto itemDtoTest = ItemDto.builder()
                .id(1)
                .name("testItem")
                .owner(2)
                .available(true)
                .build();

        Booking testBooking = Booking.builder()
                .id(4)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().minusDays(1))
                .itemId(1)
                .bookerId(21)
                .status(BookingStatus.Status.APPROVED)
                .build();

        when(itemServiceImpl.getItem(anyInt(), any()))
                .thenReturn(itemDtoTest);
        when(userServiceImpl.getUser(anyInt()))
                .thenReturn(new UserDto());

        Throwable exception = assertThrows(
                IncorrectParamInRequestException.class,
                () -> bookingServiceImpl.addBooking(testBooking)
        );

        assertEquals("Даты начала и завершения указаны неверно!", exception.getMessage());
    }

    @Test
    void addBooking_whenOwnerItIsUser() {
        ItemDto itemDtoTest = ItemDto.builder()
                .id(1)
                .name("testItem")
                .owner(2)
                .available(true)
                .build();

        Booking testBooking = Booking.builder()
                .id(4)
                .start(LocalDateTime.now().plusDays(5))
                .end(LocalDateTime.now().plusDays(10))
                .itemId(1)
                .bookerId(2)
                .status(BookingStatus.Status.APPROVED)
                .build();

        when(itemServiceImpl.getItem(anyInt(), any()))
                .thenReturn(itemDtoTest);
        when(userServiceImpl.getUser(anyInt()))
                .thenReturn(new UserDto());

        Throwable exception = assertThrows(
                UserNotFoundException.class,
                () -> bookingServiceImpl.addBooking(testBooking)
        );

        assertEquals("Невозможно создать запрос на аренду!", exception.getMessage());
    }

    @Test
    void addBooking() {
        ItemDto itemDtoTest = ItemDto.builder()
                .id(1)
                .name("testItem")
                .owner(2)
                .available(true)
                .build();

        Booking testBooking = Booking.builder()
                .id(4)
                .start(LocalDateTime.now().plusDays(5))
                .end(LocalDateTime.now().plusDays(10))
                .itemId(1)
                .bookerId(21)
                .status(BookingStatus.Status.APPROVED)
                .build();

        when(itemServiceImpl.getItem(anyInt(), any()))
                .thenReturn(itemDtoTest);
        when(userServiceImpl.getUser(anyInt()))
                .thenReturn(new UserDto());
        when(bookingRepository.save(any()))
                .thenReturn(testBooking);

        BookingDto bookingDto = bookingServiceImpl.addBooking(testBooking);

        assertEquals(bookingDto.getId(), testBooking.getId());
    }

    @Test
    void allBookings_withoutUser() {
        when(userServiceImpl.getUser(anyInt()))
                .thenReturn(null);

        Throwable exception = assertThrows(
                UserNotFoundException.class,
                () -> bookingServiceImpl.allBookings(1, "all", 0, 1)
        );

        assertEquals("Пользователя с таким ID = 1 не существует!", exception.getMessage());

    }

    @Test
    void allBookings_withCurrentState() {
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
    void allBookings_withCurrentStateAndFromIsNullSizeIsNull() {
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

        List<BookingDto> current = bookingServiceImpl.allBookings(1, state, null, null);

        assertEquals(2, current.size());
    }

    @Test
    void allBookings_withPastState() {
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
    void allBookings_withFutureState() {
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
    void allBookings_withWaitingState() {
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
    void allBookings_withRejectedState() {
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
    void allBookings_withAllState() {
        String state = "aLl";

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
        PageImpl<Booking> bookings1 = new PageImpl<>(bookings);

        when(bookingRepository.findAll(any(Pageable.class)))
                .thenReturn(bookings1);

        when(itemServiceImpl.getItem(anyInt(), any()))
                .thenReturn(itemDto);

        List<BookingDto> current = bookingServiceImpl.allBookings(1, state, 0, 1);

        assertEquals(2, current.size());
    }

    @Test
    void allBookings_withoutState() {
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
    void approveBooking_whenUserIsBooker() {

        when(bookingRepository.getById(anyInt()))
                .thenReturn(testBooking);

        Throwable exception = assertThrows(
                UserNotFoundException.class,
                () -> bookingServiceImpl.approveBooking(21, 2, true)
        );

        assertEquals("You're a booker, not owner!", exception.getMessage());

    }

    @Test
    void approveBooking_whenUserIsNotOwner() {

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
    void approveBooking_whenUBookingStatusIsApproved() {

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

    @Test
    void approveBooking_approvedTrue() {
        Booking testBooking = Booking.builder()
                .id(4)
                .start(LocalDateTime.now().plusDays(5))
                .end(LocalDateTime.now().plusDays(10))
                .itemId(1)
                .bookerId(21)
                .status(BookingStatus.Status.WAITING)
                .build();

        when(bookingRepository.getById(anyInt()))
                .thenReturn(testBooking);
        when(itemServiceImpl.getItem(anyInt(), any()))
                .thenReturn(itemDto);
        when(bookingRepository.save(any()))
                .thenReturn(testBooking);

        BookingDto bookingDto = bookingServiceImpl.approveBooking(2, 2, true);

        assertNotNull(bookingDto);
        assertEquals(BookingStatus.Status.APPROVED, bookingDto.getStatus());

    }

    @Test
    void approveBooking_approvedFalse() {
        Booking testBooking = Booking.builder()
                .id(4)
                .start(LocalDateTime.now().plusDays(5))
                .end(LocalDateTime.now().plusDays(10))
                .itemId(1)
                .bookerId(21)
                .status(BookingStatus.Status.WAITING)
                .build();

        when(bookingRepository.getById(anyInt()))
                .thenReturn(testBooking);
        when(itemServiceImpl.getItem(anyInt(), any()))
                .thenReturn(itemDto);
        when(bookingRepository.save(any()))
                .thenReturn(testBooking);

        BookingDto bookingDto = bookingServiceImpl.approveBooking(2, 2, false);

        assertNotNull(bookingDto);
        assertEquals(BookingStatus.Status.REJECTED, bookingDto.getStatus());

    }

    @Test
    void allOwnerBookings_withoutUser() {
        when(userServiceImpl.getUser(anyInt()))
                .thenReturn(null);

        Throwable exception = assertThrows(
                UserNotFoundException.class,
                () -> bookingServiceImpl.allOwnerBookings(1, "all", 0, 1)
        );

        assertEquals("Пользователя с таким ID = 1 не существует!", exception.getMessage());

    }

    @Test
    void allOwnerBookings_fromAndSizeNullableStateIsAll() {
        String state = "aLl";

        when(userServiceImpl.getUser(anyInt()))
                .thenReturn(userDto);
        when(itemServiceImpl.allOwnerItems(anyInt()))
                .thenReturn(List.of(itemDto));
        when(bookingRepository.findAllByItemId(anyInt(), any()))
                .thenReturn(new PageImpl<>(List.of(testBooking)));
        when(itemServiceImpl.getItem(anyInt(), any()))
                .thenReturn(itemDto);

        List<BookingDto> all = bookingServiceImpl.allOwnerBookings(1, state, null, null);

        assertEquals(1, all.size());

    }

    @Test
    void allOwnerBookings_stateIsCurrentAndStartTimeBeforeCurrentAndEnaTimeIsAfterCurrentTime() {
        String state = "cuRrenT";

        Booking testBooking = Booking.builder()
                .id(4)
                .start(LocalDateTime.now().minusDays(5))
                .end(LocalDateTime.now().plusDays(10))
                .itemId(1)
                .bookerId(21)
                .status(BookingStatus.Status.WAITING)
                .build();

        when(userServiceImpl.getUser(anyInt()))
                .thenReturn(userDto);
        when(itemServiceImpl.allOwnerItems(anyInt()))
                .thenReturn(List.of(itemDto));
        when(bookingRepository.findAllByItemId(anyInt(), any()))
                .thenReturn(new PageImpl<>(List.of(testBooking, testBooking)));
        when(itemServiceImpl.getItem(anyInt(), any()))
                .thenReturn(itemDto);

        List<BookingDto> all = bookingServiceImpl.allOwnerBookings(1, state, 0, 2);

        assertEquals(2, all.size());

    }

    @Test
    void allOwnerBookings_stateIsPastAndEndTimeBeforeCurrentTime() {
        String state = "pasT";

        Booking testBooking = Booking.builder()
                .id(4)
                .start(LocalDateTime.now().minusDays(10))
                .end(LocalDateTime.now().minusDays(5))
                .itemId(1)
                .bookerId(2)
                .status(BookingStatus.Status.APPROVED)
                .build();

        when(userServiceImpl.getUser(anyInt()))
                .thenReturn(userDto);
        when(itemServiceImpl.allOwnerItems(anyInt()))
                .thenReturn(List.of(itemDto));
        when(bookingRepository.findAllByItemId(anyInt(), any()))
                .thenReturn(new PageImpl<>(List.of(testBooking, testBooking, testBooking)));
        when(itemServiceImpl.getItem(anyInt(), any()))
                .thenReturn(itemDto);

        List<BookingDto> all = bookingServiceImpl.allOwnerBookings(1, state, 0, 3);

        assertEquals(3, all.size());

    }

    @Test
    void allOwnerBookings_stateIsFutureAndStartTimeAfterCurrent() {
        String state = "fuTure";

        Booking testBooking = Booking.builder()
                .id(4)
                .start(LocalDateTime.now().plusDays(5))
                .end(LocalDateTime.now().plusDays(10))
                .itemId(1)
                .bookerId(2)
                .status(BookingStatus.Status.APPROVED)
                .build();

        when(userServiceImpl.getUser(anyInt()))
                .thenReturn(userDto);
        when(itemServiceImpl.allOwnerItems(anyInt()))
                .thenReturn(List.of(itemDto));
        when(bookingRepository.findAllByItemId(anyInt(), any()))
                .thenReturn(new PageImpl<>(List.of(testBooking, testBooking, testBooking)));
        when(itemServiceImpl.getItem(anyInt(), any()))
                .thenReturn(itemDto);

        List<BookingDto> all = bookingServiceImpl.allOwnerBookings(1, state, 0, 3);

        assertEquals(3, all.size());

    }

    @Test
    void allOwnerBookings_stateIsWaiting() {
        String state = "waitiNg";

        Booking testBooking = Booking.builder()
                .id(4)
                .start(LocalDateTime.now().plusDays(5))
                .end(LocalDateTime.now().plusDays(10))
                .itemId(1)
                .bookerId(2)
                .status(BookingStatus.Status.APPROVED)
                .build();

        Booking testBookingWaiting = Booking.builder()
                .id(4)
                .start(LocalDateTime.now().plusDays(5))
                .end(LocalDateTime.now().plusDays(10))
                .itemId(1)
                .bookerId(2)
                .status(BookingStatus.Status.WAITING)
                .build();

        when(userServiceImpl.getUser(anyInt()))
                .thenReturn(userDto);
        when(itemServiceImpl.allOwnerItems(anyInt()))
                .thenReturn(List.of(itemDto));
        when(bookingRepository.findAllByItemId(anyInt(), any()))
                .thenReturn(new PageImpl<>(List.of(testBooking, testBooking, testBooking, testBookingWaiting)));
        when(itemServiceImpl.getItem(anyInt(), any()))
                .thenReturn(itemDto);

        List<BookingDto> all = bookingServiceImpl.allOwnerBookings(1, state, 0, 3);

        assertEquals(1, all.size());

    }

    @Test
    void allOwnerBookings_stateIsRejected() {
        String state = "Rejected";

        Booking testBooking = Booking.builder()
                .id(4)
                .start(LocalDateTime.now().plusDays(5))
                .end(LocalDateTime.now().plusDays(10))
                .itemId(1)
                .bookerId(2)
                .status(BookingStatus.Status.APPROVED)
                .build();

        Booking testBookingRejected = Booking.builder()
                .id(4)
                .start(LocalDateTime.now().plusDays(5))
                .end(LocalDateTime.now().plusDays(10))
                .itemId(1)
                .bookerId(2)
                .status(BookingStatus.Status.REJECTED)
                .build();

        when(userServiceImpl.getUser(anyInt()))
                .thenReturn(userDto);
        when(itemServiceImpl.allOwnerItems(anyInt()))
                .thenReturn(List.of(itemDto));
        when(bookingRepository.findAllByItemId(anyInt(), any()))
                .thenReturn(new PageImpl<>(List.of(testBooking, testBooking, testBooking, testBookingRejected, testBookingRejected)));
        when(itemServiceImpl.getItem(anyInt(), any()))
                .thenReturn(itemDto);

        List<BookingDto> all = bookingServiceImpl.allOwnerBookings(1, state, 0, 3);

        assertEquals(2, all.size());

    }

    @Test
    void allOwnerBookings_withoutState() {
        String state = "";

        when(userServiceImpl.getUser(anyInt()))
                .thenReturn(userDto);

        Throwable exception = assertThrows(
                IncorrectParamInRequestException.class,
                () -> bookingServiceImpl.allOwnerBookings(1, state, 0, 1)
        );

        assertEquals("Unknown state: UNSUPPORTED_STATUS", exception.getMessage());


    }
}
