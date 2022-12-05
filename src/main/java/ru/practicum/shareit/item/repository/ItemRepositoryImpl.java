package ru.practicum.shareit.item.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.MapRowToItemDto;

import java.util.List;

import static ru.practicum.shareit.item.sql.ItemSqlRequest.*;

@Repository
@RequiredArgsConstructor
public class ItemRepositoryImpl implements ItemRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public ItemDto getItem(int id) {

        return jdbcTemplate.queryForObject(SQL_GET_ITEM_BY_ID, new MapRowToItemDto(), id);
    }

    @Transactional
    @Override
    public ItemDto addItem(ItemDto itemDto) {
        jdbcTemplate.update(SQL_ADD_ITEM, itemDto.getName(), itemDto.getDescription(), itemDto.getAvailable());

        return getItem(itemDto.getId());
    }

    @Transactional
    @Override
    public ItemDto updateItem(ItemDto itemDto) {
        return null;
    }

    @Override
    public List<ItemDto> searchItem(String description) {
        description = "%" + description + "%";

        return jdbcTemplate.query(SQL_SEARCH_ITEM_BY_DESCRIPTION, new MapRowToItemDto(), description.toLowerCase());
    }

    @Override
    public List<ItemDto> allItems() {
        return jdbcTemplate.query(SQL_GET_ALL_ITEMS, new MapRowToItemDto());
    }

    @Override
    public void deleteItem(int id) {
        jdbcTemplate.update(SQL_DELETE_ITEM_BY_ID, id);
    }

    @Override
    public void deleteAllItems() {

    }

}
