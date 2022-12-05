package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto getItem(int id);

    ItemDto addItem(ItemDto ItemDto);

    ItemDto updateItem(ItemDto ItemDto);

    List<ItemDto> searchItem(String description);

    List<ItemDto> allItems();

    void deleteItem(int id);

    void deleteAllItems();
}
