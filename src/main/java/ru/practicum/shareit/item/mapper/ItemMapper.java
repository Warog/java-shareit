package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ItemMapper {

    public ItemDto toItemDro(ResultSet rs, int rowNum) throws SQLException {
        return ItemDto.builder()
                .id(1)
                .name("")
                .description("")
                .available(true)
                .owner(rs.getInt("owner"))
                .request(rs.getInt("request"))
                .build();
    }

    public Item toItem() {
        return Item.builder()
                .id(1)
                .name("")
                .description("")
                .available(true)
//                .owner(User.builder().build())
//                .request(new ItemRequest())
                .build();
    }
}
