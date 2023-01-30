package ru.practicum.shareit.item.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.mapper.CommentMapper;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.comment.repository.CommentRepository;
import ru.practicum.shareit.exception.CommentDeniedException;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.exception.UserNotOwnerException;
import ru.practicum.shareit.item.dto.ItemBookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class ItemServiceImpl implements ItemService {
    ItemRepository itemRepository;
    BookingRepository bookingRepository;
    CommentRepository commentRepository;
    UserRepository userRepository;

    public ItemServiceImpl(ItemRepository itemRepository, BookingRepository bookingRepository, CommentRepository commentRepository, UserRepository userRepository) {
        this.itemRepository = itemRepository;
        this.bookingRepository = bookingRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public ItemDto getItem(int id, Integer userId) {
        if (itemRepository.countItemById(id) < 1)
            throw new ItemNotFoundException(String.format("Предмет ID=%d не найден", id));
        if (userRepository.countUserById(userId) < 1)
            throw new UserNotFoundException("Пользователя с указанным Id не существует!");

        Item item = itemRepository.getById(id);
        List<Comment> comments = commentRepository.findCommentsByItemId(id);

        Optional<ItemBookingDto> lastBooking = Optional.empty();
        Optional<ItemBookingDto> nextBooking = Optional.empty();

        if (item.getOwner().equals(userId)) {

            List<Booking> allBookings = bookingRepository.findAllByItemId(item.getId());

            lastBooking = allBookings.stream()
                    .filter(bookingDto -> bookingDto.getStatus().equals(BookingStatus.Status.APPROVED))
                    .min(Comparator.comparing(Booking::getStart))
                    .map(booking -> ItemBookingDto.builder()
                            .id(booking.getId())
                            .bookerId(booking.getBookerId())
                            .build());

            nextBooking = allBookings.stream()
                    .filter(booking -> booking.getStatus().equals(BookingStatus.Status.APPROVED))
                    .max(Comparator.comparing(Booking::getStart))
                    .map(booking -> ItemBookingDto.builder()
                            .id(booking.getId())
                            .bookerId(booking.getBookerId())
                            .build());
        }

        List<CommentDto> commentDtos = comments.stream()
                .map(comment -> CommentMapper.toCommentDto(commentRepository.save(comment), userRepository.getById(comment.getAuthorId())))
                .collect(Collectors.toList());

        return ItemMapper.toItemDtoWithBookings(itemRepository.getById(id),
                lastBooking.orElse(null),
                nextBooking.orElse(null),
                commentDtos
        );
    }

    @Transactional
    @Override
    public ItemDto addItem(Integer ownerId, ItemDto itemDto) {
        if (userRepository.countUserById(ownerId) < 1)
            throw new UserNotFoundException("Пользователя с указанным Id не существует!");

        itemDto.setOwner(ownerId);
        ItemDto addedItem = ItemMapper.toItemDto(itemRepository.save(ItemMapper.toItem(itemDto)));

        addedItem.setRequestId(itemDto.getRequestId());

        if (itemDto.getRequestId() != null) {
            itemRepository.addItemWithRequest(itemDto.getRequestId(), addedItem.getId());
        }

        return addedItem;
    }

    @Transactional
    @Override
    public ItemDto updateItem(Integer ownerId, ItemDto itemDto) {
        Item updatedItem = itemRepository.getById(itemDto.getId());

        if (!updatedItem.getOwner().equals(ownerId))
            throw new UserNotOwnerException("Пользователь не является владельцем!");

        if (itemDto.getName() != null)
            updatedItem.setName(itemDto.getName());
        if (itemDto.getDescription() != null)
            updatedItem.setDescription(itemDto.getDescription());
        if (itemDto.getAvailable() != null)
            updatedItem.setAvailable(itemDto.getAvailable());

        return ItemMapper.toItemDto(itemRepository.save(updatedItem));
    }

    @Transactional
    @Override
    public List<Item> searchItem(String description) {
        return itemRepository.searchAllByDescriptionContainingIgnoreCaseAndAvailableTrue(description);
    }

    @Transactional
    @Override
    public List<ItemDto> allOwnerItems(int ownerId) {
        List<Item> items = itemRepository.findAllByOwnerOrderByIdAsc(ownerId);

        return items.stream()
                .map(item -> getItem(item.getId(), ownerId)).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public CommentDto addComment(int authorId, int itemId, Comment comment) {
        LocalDateTime nowDate = LocalDateTime.now();
        List<Booking> bookings = bookingRepository.findAllByItemIdAndBookerIdAndStatusAndEndBefore(itemId, authorId, BookingStatus.Status.APPROVED, nowDate);

        if (bookings.size() == 0)
            throw new CommentDeniedException("Комментарий отклонен!");

        comment.setItemId(itemId);
        comment.setAuthorId(authorId);
        comment.setCreated(nowDate);

        return CommentMapper.toCommentDto(commentRepository.save(comment), userRepository.getById(authorId));
    }
}
