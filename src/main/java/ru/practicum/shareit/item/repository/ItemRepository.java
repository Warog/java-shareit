package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemRepository {
    ItemDto getItem(int id);

    ItemDto addItem(Integer ownerId, ItemDto itemDto);

    ItemDto updateItem(Integer ownerId, ItemDto itemDto);

    List<ItemDto> searchItem(String description);

    List<ItemDto> allItems();

    List<ItemDto> allOwnerItems(int ownerId);

    void deleteItem(int id);

    void deleteAllItems();
}
