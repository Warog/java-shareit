package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemRepository {
    ItemDto getItem(int id);

    ItemDto addItem(ItemDto ItemDto);

    ItemDto updateItem(ItemDto ItemDto);

    List<ItemDto> searchItem(String description);

    List<ItemDto> allItems();

    void deleteItem(int id);

    void deleteAllItems();
}
