package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    ItemDto getItem(int id);

    ItemDto addItem(Integer ownerId, ItemDto itemDto);

    ItemDto updateItem(Integer ownerId, ItemDto itemDto);

    List<Item> searchItem(String description);

    List<Item> allItems();

    List<Item> allOwnerItems(int ownerId);

    void deleteItem(int id);

    void deleteAllItems();
}
