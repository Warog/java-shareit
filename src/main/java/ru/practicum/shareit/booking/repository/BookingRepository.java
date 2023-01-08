package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {
    List<Booking> findByStatusAndBookerIdOrderByStartDesc(BookingStatus.Status status, int bookerId);
    List<Booking> findByBookerIdAndEndIsAfterAndStartIsBeforeOrderByStartDesc(int bookerId, LocalDateTime endAfter, LocalDateTime startBefore);
    List<Booking> findAllByBookerIdOrderByEndDesc(int bookerId);
    List<Booking> findByEndIsBefore(LocalDateTime end, Sort sort);
    List<Booking> findByBookerIdAndStartIsAfter(int bookerId, LocalDateTime start, Sort sort);
    List<Booking> findAllByItemId(int itemId);
    List<Booking> findAllByItemIdAndBookerIdAndStatusAndEndIsBefore(int itemId, int bookerId, BookingStatus.Status status, LocalDateTime endIsBeforeTime);
    long countBookingsById(int id);
}
