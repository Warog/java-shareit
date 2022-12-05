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
    public ItemDto addItem(ItemDto itemDto) {
        return itemRepository.addItem(itemDto);
    }

    @Override
    public ItemDto updateItem(ItemDto ItemDto) {
        return null;
    }

    @Override
    public List<ItemDto> searchItem(String description) {
        return itemRepository.searchItem(description);
    }

    @Override
    public List<ItemDto> allItems() {
        return null;
    }

    @Override
    public void deleteItem(int id) {

    }

    @Override
    public void deleteAllItems() {

    }
}
