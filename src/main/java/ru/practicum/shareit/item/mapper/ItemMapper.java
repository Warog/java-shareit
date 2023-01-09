package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemBookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public class ItemMapper {

    public static ItemDto toItemDto(Item item) {
        if (item != null)
            return ItemDto.builder()
                    .id(item.getId())
                    .name(item.getName())
                    .description(item.getDescription())
                    .available(item.getAvailable())
                    .owner(item.getOwner())
                    .build();

        return ItemDto.builder().build();
    }

    public static Item toItem(ItemDto itemDto) {
        if (itemDto != null)
            return Item.builder()
                    .id(itemDto.getId())
                    .name(itemDto.getName())
                    .description(itemDto.getDescription())
                    .available(itemDto.getAvailable())
                    .owner(itemDto.getOwner())
                    .build();

        return Item.builder().build();
    }

    public static ItemDto toItemDtoWithBookings(Item item, ItemBookingDto lastBooking, ItemBookingDto nextBooking, List<CommentDto> comments) {
        if (item != null)
            return ItemDto.builder()
                    .id(item.getId())
                    .name(item.getName())
                    .description(item.getDescription())
                    .available(item.getAvailable())
                    .owner(item.getOwner())
                    .lastBooking(lastBooking)
                    .nextBooking(nextBooking)
                    .comments(comments)
                    .build();

        return ItemDto.builder().build();
    }
}
