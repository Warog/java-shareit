package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.sql.ResultSet;

public class ItemMapper {

    public ItemDto toItemDro(ResultSet rs, int rowNum) {
        return ItemDto.builder()
                .id(1)
                .name("")
                .description("")
                .available(true)
                .owner(User.builder().build())
                .request(new ItemRequest())
                .build();
    }

    public Item toItem() {
        return Item.builder()
                .id(1)
                .name("")
                .description("")
                .available(true)
                .owner(User.builder().build())
                .request(new ItemRequest())
                .build();
    }
}
