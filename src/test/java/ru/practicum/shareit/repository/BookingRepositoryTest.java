package ru.practicum.shareit.repository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.PersistenceConfig;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@Sql(scripts = {"file:src/test/java/resources/test_data.sql"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(PersistenceConfig.class)
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:file:./db/shareitTest"
})
public class BookingRepositoryTest {

    @Autowired
    BookingRepository bookingRepository;

    @BeforeAll
    static void setUp() {

    }

    @Test
    void findAllBookingsByItemId() {
        List<Booking> bookings = bookingRepository.findAllByItemId(2);

        assertEquals(3, bookings.size());
        assertEquals(2, bookings.get(0).getItemId());
        assertEquals(4, bookings.get(0).getBookerId());
    }

    @Test
    void findAllBookingsByItemId_withPagination() {
        List<Booking> bookings = bookingRepository.findAllByItemId(2, PageRequest.of(0, 1, Sort.unsorted())).toList();

        assertEquals(1, bookings.size());

        List<Booking> bookingsTwo = bookingRepository.findAllByItemId(2, PageRequest.of(0, 2, Sort.unsorted())).toList();

        assertEquals(2, bookingsTwo.size());

    }

    @Test
    void countBookingsById() {
        long l = bookingRepository.countBookingsById(1);

        assertEquals(1, l);

    }

    @Test
    void findAll_withPagination() {
        Page<Booking> all = bookingRepository.findAll(PageRequest.of(0, 2, Sort.unsorted()));

        assertEquals(2, all.toList().size());

        Page<Booking> allTwo = bookingRepository.findAll(PageRequest.of(1, 2, Sort.unsorted()));

        assertEquals(2, allTwo.toList().size());
    }

    @Test
    void findByEndIsBefore() {
        List<Booking> bookings = bookingRepository.findByEndIsBefore(LocalDateTime.of(2022, 1, 1, 1, 1), Sort.by(Sort.Direction.DESC, "start"));

        assertEquals(4, bookings.size());

        List<Booking> bookingsTwo = bookingRepository.findByEndIsBefore(LocalDateTime.of(2021, 1, 1, 1, 1), Sort.by(Sort.Direction.DESC, "start"));

        assertEquals(3, bookingsTwo.size());
    }

    @Test
    void findAllByItemIdAndBookerIdAndStatusAndEndIsBefore() {
        List<Booking> bookings = bookingRepository.findAllByItemIdAndBookerIdAndStatusAndEndIsBefore(2, 4, BookingStatus.Status.REJECTED, LocalDateTime.of(2021, 1, 1, 1, 1));

        assertEquals(1, bookings.size());
        assertEquals(2, bookings.get(0).getItemId());
        assertEquals(4, bookings.get(0).getBookerId());
    }

    @Test
    void getByBookerIdAndEndIsAfterAndStartIsBeforeOrderByStartDesc_withPagination() {
        List<Booking> bookings = bookingRepository.getByBookerIdAndEndIsAfterAndStartIsBeforeOrderByStartDesc(4, LocalDateTime.of(2020, 1, 1, 1, 1), LocalDateTime.of(2021, 1, 1, 1, 1), PageRequest.of(0, 1, Sort.unsorted())).toList();

        assertEquals(1, bookings.size());
        assertEquals(2, bookings.get(0).getItemId());
        assertEquals(4, bookings.get(0).getBookerId());

    }
}
