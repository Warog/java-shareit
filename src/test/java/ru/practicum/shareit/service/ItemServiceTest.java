package ru.practicum.shareit.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.comment.repository.CommentRepository;
import ru.practicum.shareit.exception.CommentDeniedException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Transactional
@SpringBootTest(
        properties = "spring.datasource.url=jdbc:h2:file:./db/shareitTest"
)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {
    ItemService itemService;
    @Mock
    ItemRepository itemRepository;
    @Mock
    BookingRepository bookingRepository;
    @Mock
    CommentRepository commentRepository;
    @Mock
    UserRepository userRepository;

    private final Item testItem = Item.builder()
            .id(1)
            .name("A")
            .description("B")
            .owner(1)
            .available(true)
            .requests(Set.of(new Request(1, "A", 2, LocalDateTime.of(2023, 1, 22, 1, 1), null), new Request(2, "B", 3, LocalDateTime.of(2023, 1, 22, 2, 2), null)))
            .build();

    @BeforeEach
    void setUp() {
        itemService = new ItemServiceImpl(itemRepository, bookingRepository, commentRepository, userRepository);
    }

    @Test
    void getNotOwnerUserItem() {

        Mockito
                .when(itemRepository.getItem(anyInt()))
                .thenReturn(testItem);

        ItemDto item = itemService.getItem(1, 2);

        assertNull(item.getLastBooking(), "Неверный вывод истории аренд");
        assertNull(item.getNextBooking());
    }

    @Test
    void getOwnerUserItem() {

        when(itemRepository.getItem(anyInt()))
                .thenReturn(testItem);

        LocalDateTime date = LocalDateTime.now();
        List<Booking> bookings = List.of(new Booking(1, date, date.plusDays(1), 1, 3, BookingStatus.Status.APPROVED));

        when(bookingRepository.findAllByItemId(anyInt()))
                .thenReturn(bookings);

        ItemDto item = itemService.getItem(1, 1);

        assertNotNull(item.getLastBooking(), "Неверный вывод истории аренд");
        assertNotNull(item.getNextBooking());
    }

    @Test
    void addCommentWithoutBookings() {

        LocalDateTime date = LocalDateTime.now();
        List<Booking> bookings = Collections.emptyList();
        Comment comment = Comment.builder()
                .id(1)
                .text("Super")
                .itemId(1)
                .created(date)
                .authorId(1)
                .build();

        when(bookingRepository.findAllByItemIdAndBookerIdAndStatusAndEndIsBefore(anyInt(), anyInt(), any(BookingStatus.Status.class), any(LocalDateTime.class)))
                .thenReturn(bookings);

        Throwable exception = assertThrows(
                CommentDeniedException.class,
                () -> itemService.addComment(1, 1, comment)
        );
        assertEquals("Комментарий отклонен!", exception.getMessage());

    }

    @Test
    void addCommentWithBookings() {

        LocalDateTime date = LocalDateTime.now();
        List<Booking> bookings = List.of(new Booking(1, date, date.plusDays(1), 1, 3, BookingStatus.Status.APPROVED));
        Comment comment = Comment.builder()
                .id(1)
                .text("Super")
                .itemId(1)
                .created(date)
                .authorId(1)
                .build();

        when(bookingRepository.findAllByItemIdAndBookerIdAndStatusAndEndIsBefore(anyInt(), anyInt(), any(BookingStatus.Status.class), any(LocalDateTime.class)))
                .thenReturn(bookings);

        when(userRepository.getUser(anyInt()))
                .thenReturn(new User(2, "Hero", "123@mail.ru"));

        when(commentRepository.save(any(Comment.class)))
                .thenReturn(comment);

        CommentDto commentDto = itemService.addComment(1, 1, comment);

        assertNotNull(commentDto);
        assertEquals(1, commentDto.getId());
        assertEquals("Super", commentDto.getText());

    }
}
