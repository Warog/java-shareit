package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {
    Item getItem(int id);

    Item addItem(Integer ownerId, Item item);

    Item updateItem(Integer ownerId, ItemDto itemDto);

    List<Item> searchItem(String description);

    List<Item> allOwnerItems(int ownerId);

    void addItemWithRequest(ItemDto itemDto);
}
