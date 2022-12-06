package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.repository.ItemRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;

    @Override
    public ItemDto getItem(int id) {
        return itemRepository.getItem(id);
    }

    @Override
    public ItemDto addItem(Integer ownerId, ItemDto itemDto) {
        return itemRepository.addItem(ownerId, itemDto);
    }

    @Override
    public ItemDto updateItem(Integer ownerId, ItemDto itemDto) {
        return itemRepository.updateItem(ownerId, itemDto);
    }

    @Override
    public List<ItemDto> searchItem(String description) {
        return itemRepository.searchItem(description);
    }

    @Override
    public List<ItemDto> allItems() {
        return itemRepository.allItems();
    }

    @Override
    public List<ItemDto> allOwnerItems(int ownerId) {
        return itemRepository.allOwnerItems(ownerId);
    }

    @Override
    public void deleteItem(int id) {

    }

    @Override
    public void deleteAllItems() {

    }
}
