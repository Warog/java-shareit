package ru.practicum.shareit.item.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.dto.ItemBookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;

    public ItemServiceImpl(ItemRepository itemRepository, BookingRepository bookingRepository) {
        this.itemRepository = itemRepository;
        this.bookingRepository = bookingRepository;
    }

    @Transactional
    @Override
    public ItemDto getItem(int id, Integer userId) {
        Item item = itemRepository.getItem(id);

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

        return ItemMapper.toItemDtoWithBookings(itemRepository.getItem(id), lastBooking.orElse(null), nextBooking.orElse(null));
    }

    @Transactional
    @Override
    public ItemDto addItem(Integer ownerId, ItemDto itemDto) {
        return ItemMapper.toItemDto(itemRepository.addItem(ownerId, ItemMapper.toItem(itemDto)));
    }

    @Transactional
    @Override
    public ItemDto updateItem(Integer ownerId, ItemDto itemDto) {
        return ItemMapper.toItemDto(itemRepository.updateItem(ownerId, itemDto));
    }

    @Transactional
    @Override
    public List<Item> searchItem(String description) {
        return itemRepository.searchItem(description);
    }

    @Transactional
    @Override
    public List<Item> allItems() {
        return itemRepository.allItems();
    }

    @Transactional
    @Override
    public List<ItemDto> allOwnerItems(int ownerId) {
        List<Item> items = itemRepository.allOwnerItems(ownerId);

        return items.stream()
                .map(item -> getItem(item.getId(), ownerId)).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void deleteItem(int id) {

    }

    @Transactional
    @Override
    public void deleteAllItems() {

    }
}
