package ru.practicum.shareit.booking.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.IncorrectParamInRequestException;
import ru.practicum.shareit.exception.ItemNotAvailableException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.exception.UserNotOwnerException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class BookingServiceImpl implements BookingService {
    BookingRepository bookingRepository;
    ItemService itemService;
    UserService userService;

    public BookingServiceImpl(BookingRepository bookingRepository, ItemServiceImpl itemServiceImpl, UserServiceImpl userServiceImpl) {
        this.bookingRepository = bookingRepository;
        this.itemService = itemServiceImpl;
        this.userService = userServiceImpl;
    }

    @Transactional
    @Override
    public BookingDto getBooking(int userId, int id) {
        if (bookingRepository.countBookingsById(id) < 1)
            throw new UserNotFoundException(String.format("Пользователя с таким ID = %d не существует!", userId));

        Booking booking = bookingRepository.getById(id);

        if (!itemService.getItem(booking.getItemId(), null).getOwner().equals(userId) && !booking.getBookerId().equals(userId))
            throw new UserNotFoundException("Доступ запрещен!");

        return BookingMapper.toBookingDto(booking, itemService.getItem(booking.getItemId(), null));
    }

    @Transactional
    @Override
    public BookingDto addBooking(Booking booking) {
//        LocalDateTime currentTime = ZonedDateTime.now(ZoneId.ofOffset("UTC", ZoneOffset.of("+03"))).toLocalDateTime();
        LocalDateTime currentTime = LocalDateTime.now();
        ItemDto item = itemService.getItem(booking.getItemId(), null);

        if (userService.getUser(booking.getBookerId()) == null)
            throw new UserNotFoundException(String.format("Пользователя с таким ID = %d не существует!", booking.getBookerId()));
        if (!item.getAvailable())
            throw new ItemNotAvailableException("Данный предмет недоступен!");
        if (booking.getEnd().isBefore(currentTime)
                || booking.getStart().isBefore(currentTime)
                || booking.getEnd().isBefore(booking.getStart())
        )
            throw new IncorrectParamInRequestException("Даты начала и завершения указаны неверно!");
        if (booking.getBookerId().equals(item.getOwner())) {
            throw new UserNotFoundException("Невозможно создать запрос на аренду!");
        }

        booking.setStatus(BookingStatus.Status.WAITING);

        return BookingMapper.toBookingDto(bookingRepository.save(booking), itemService.getItem(booking.getItemId(), null));
    }

    @Transactional
    @Override
    public List<BookingDto> allBookings(int bookerId, String state, Integer from, Integer size) {
        Pageable pageable;

        if (userService.getUser(bookerId) == null)
            throw new UserNotFoundException(String.format("Пользователя с таким ID = %d не существует!", bookerId));
        if (from == null || size == null) {
            pageable = Pageable.unpaged();
        } else {
            pageable = PageRequest.of(from, size, Sort.by(Sort.Direction.DESC, "end"));
        }

//        LocalDateTime currentTime = ZonedDateTime.now(ZoneId.ofOffset("UTC", ZoneOffset.of("+03"))).toLocalDateTime();
        LocalDateTime currentTime = LocalDateTime.now();
        switch (state.toLowerCase()) {
            case "current":
                return bookingRepository.getByBookerIdAndEndIsAfterAndStartBeforeOrderByStartDesc(bookerId, currentTime, currentTime, pageable)
                        .stream()
                        .map(booking -> BookingMapper.toBookingDto(booking, itemService.getItem(booking.getItemId(), null)))
                        .collect(Collectors.toList());
            case "past":
                return bookingRepository.findByEndIsBefore(currentTime, Sort.by(Sort.Direction.DESC, "start"))
                        .stream()
                        .map(booking -> BookingMapper.toBookingDto(booking, itemService.getItem(booking.getItemId(), null)))
                        .collect(Collectors.toList());
            case "future":
                return bookingRepository.findByBookerIdAndStartAfter(bookerId, LocalDateTime.now(), Sort.by(Sort.Direction.DESC, "start"))
                        .stream()
                        .map(booking -> BookingMapper.toBookingDto(booking, itemService.getItem(booking.getItemId(), null)))
                        .collect(Collectors.toList());
            case "waiting":
                return bookingRepository.findByStatusAndBookerIdOrderByStartDesc(BookingStatus.Status.WAITING, bookerId)
                        .stream()
                        .map(booking -> BookingMapper.toBookingDto(booking, itemService.getItem(booking.getItemId(), null)))
                        .collect(Collectors.toList());
            case "rejected":
                return bookingRepository.findByStatusAndBookerIdOrderByStartDesc(BookingStatus.Status.REJECTED, bookerId)
                        .stream()
                        .map(booking -> BookingMapper.toBookingDto(booking, itemService.getItem(booking.getItemId(), null)))
                        .collect(Collectors.toList());
            case "all":
                List<Booking> allByBookerId = bookingRepository.findAll(pageable).toList();
                return allByBookerId
                        .stream()
                        .map(booking -> BookingMapper.toBookingDto(booking, itemService.getItem(booking.getItemId(), null)))
                        .sorted(Comparator.comparing(BookingDto::getStart).reversed())
                        .collect(Collectors.toList());
            default:
                throw new IncorrectParamInRequestException("Unknown state: UNSUPPORTED_STATUS");
        }
    }

    @Transactional
    @Override
    public List<BookingDto> allOwnerBookings(int userId, String state, Integer from, Integer size) {
        Pageable pageable;

        if (userService.getUser(userId) == null)
            throw new UserNotFoundException(String.format("Пользователя с таким ID = %d не существует!", userId));
        if (from == null || size == null) {
            pageable = Pageable.unpaged();
        } else {
            pageable = PageRequest.of(from, size, Sort.by(Sort.Direction.DESC, "end"));
        }
//        LocalDateTime currentTime = ZonedDateTime.now(ZoneId.ofOffset("UTC", ZoneOffset.of("+03"))).toLocalDateTime();
        LocalDateTime currentTime = LocalDateTime.now();
        List<ItemDto> itemList = itemService.allOwnerItems(userId);
        List<Booking> bookingList = new ArrayList<>();

        for (ItemDto item : itemList) {
            bookingList.addAll(bookingRepository.findAllByItemId(item.getId(), pageable).toList());
        }

        switch (state.toLowerCase()) {
            case "current":
                return bookingList.stream()
                        .filter(booking -> booking.getStart().isBefore(currentTime) && booking.getEnd().isAfter(currentTime))
                        .map(booking -> BookingMapper.toBookingDto(booking, itemService.getItem(booking.getItemId(), null)))
                        .sorted(Comparator.comparing(BookingDto::getStart).reversed())
                        .collect(Collectors.toList());
            case "past":
                return bookingList.stream()
                        .filter(booking -> booking.getEnd().isBefore(currentTime))
                        .map(booking -> BookingMapper.toBookingDto(booking, itemService.getItem(booking.getItemId(), null)))
                        .sorted(Comparator.comparing(BookingDto::getStart).reversed())
                        .collect(Collectors.toList());
            case "future":
                return bookingList.stream()
                        .filter(booking -> booking.getStart().isAfter(currentTime))
                        .map(booking -> BookingMapper.toBookingDto(booking, itemService.getItem(booking.getItemId(), null)))
                        .sorted(Comparator.comparing(BookingDto::getStart).reversed())
                        .collect(Collectors.toList());
            case "waiting":
                return bookingList.stream()
                        .filter(booking -> booking.getStatus().equals(BookingStatus.Status.WAITING))
                        .map(booking -> BookingMapper.toBookingDto(booking, itemService.getItem(booking.getItemId(), null)))
                        .sorted(Comparator.comparing(BookingDto::getStart).reversed())
                        .collect(Collectors.toList());
            case "rejected":
                return bookingList.stream()
                        .filter(booking -> booking.getStatus().equals(BookingStatus.Status.REJECTED))
                        .map(booking -> BookingMapper.toBookingDto(booking, itemService.getItem(booking.getItemId(), null)))
                        .sorted(Comparator.comparing(BookingDto::getStart).reversed())
                        .collect(Collectors.toList());
            case "all":
                return bookingList.stream()
                        .map(booking -> BookingMapper.toBookingDto(booking, itemService.getItem(booking.getItemId(), null)))
                        .sorted(Comparator.comparing(BookingDto::getStart).reversed())
                        .collect(Collectors.toList());
            default:
                throw new IncorrectParamInRequestException("Unknown state: UNSUPPORTED_STATUS");

        }
    }

    @Override
    public BookingDto approveBooking(int userId, int bookingId, boolean isApproved) {
        Booking booking = bookingRepository.getById(bookingId);

        if (booking.getBookerId().equals(userId))
            throw new UserNotFoundException("You're a booker, not owner!");
        if (!itemService.getItem(booking.getItemId(), null).getOwner().equals(userId))
            throw new UserNotOwnerException(String.format("Пользователь с ID = %d не является владельцем продукта", userId));
        if (booking.getStatus().equals(BookingStatus.Status.APPROVED))
            throw new IncorrectParamInRequestException("Аренда уже подтверждена");

        if (isApproved)
            booking.setStatus(BookingStatus.Status.APPROVED);
        else
            booking.setStatus(BookingStatus.Status.REJECTED);


        return BookingMapper.toBookingDto(bookingRepository.save(booking), itemService.getItem(booking.getItemId(), null));
    }
}
