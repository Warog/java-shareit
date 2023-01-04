package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;

    @Transactional
    @Override
    public ItemDto getItem(int id) {
        return ItemMapper.toItemDto(itemRepository.getItem(id));
    }

    @Transactional
    @Override
    public ItemDto addItem(Integer ownerId, ItemDto itemDto) {
        return ItemMapper.toItemDto(itemRepository.addItem(ownerId, ItemMapper.toItem(itemDto)));
    }

    @Transactional
    @Override
    public ItemDto updateItem(Integer ownerId, ItemDto itemDto) {
        return ItemMapper.toItemDto(itemRepository.updateItem(ownerId, itemDto));
    }

    @Transactional
    @Override
    public List<Item> searchItem(String description) {
        return itemRepository.searchItem(description);
    }

    @Transactional
    @Override
    public List<Item> allItems() {
        return itemRepository.allItems();
    }

    @Transactional
    @Override
    public List<Item> allOwnerItems(int ownerId) {
        return itemRepository.allOwnerItems(ownerId);
    }

    @Transactional
    @Override
    public void deleteItem(int id) {

    }

    @Transactional
    @Override
    public void deleteAllItems() {

    }
}
