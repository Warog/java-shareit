package ru.practicum.shareit.item.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.exception.UserNotOwnerException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.MapRowToItemDto;
import ru.practicum.shareit.item.validate.ValidateItem;

import java.util.List;
import java.util.Optional;

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
    public ItemDto addItem(Integer ownerId, ItemDto itemDto) {

        try {
            jdbcTemplate.queryForObject(SQL_FOUND_USER, Integer.class, ownerId);
        } catch (EmptyResultDataAccessException e) {
            throw new UserNotFoundException(String.format("Владелец с ID = %d не найден!", ownerId), e.getCause());
        }

        ValidateItem.validateParamOnNull(Optional.of(itemDto));
        ValidateItem.validateOnEmptyName(Optional.of(itemDto));

        jdbcTemplate.update(SQL_ADD_ITEM, itemDto.getName(), itemDto.getDescription(), itemDto.getAvailable(), itemDto.getOwner(), itemDto.getRequest());

        return jdbcTemplate.queryForObject(SQL_GET_ITEM_BY_ALL_PARAMETERS, new MapRowToItemDto(), itemDto.getName(), itemDto.getDescription(), itemDto.getAvailable());
    }

    @Transactional
    @Override
    public ItemDto updateItem(Integer ownerId, ItemDto itemDto) {

        Integer isOwner = jdbcTemplate.queryForObject(SQL_GET_OWNER_ITEM, Integer.class, ownerId, itemDto.getId());

        if (isOwner == 1) {
            jdbcTemplate.update(SQL_UPDATE_ITEM_BY_ID, itemDto.getName(), itemDto.getDescription(), itemDto.getAvailable(), itemDto.getId());
        } else {
            throw new UserNotOwnerException("Пользователь не является владельцем!");
        }

        return getItem(itemDto.getId());
    }

    @Override
    public List<ItemDto> searchItem(String description) {
        if (description.isBlank()) return List.of();

        description = "%" + description.toLowerCase() + "%";

        return jdbcTemplate.query(SQL_SEARCH_ITEM_BY_DESCRIPTION, new MapRowToItemDto(), description);
    }

    @Override
    public List<ItemDto> allItems() {
        return jdbcTemplate.query(SQL_GET_ALL_ITEMS, new MapRowToItemDto());
    }

    @Override
    public List<ItemDto> allOwnerItems(int ownerId) {
        return jdbcTemplate.query(SQL_GET_ALL_OWNER_ITEMS_BY_OWNER_ID, new MapRowToItemDto(), ownerId);
    }

    @Override
    public void deleteItem(int id) {
        jdbcTemplate.update(SQL_DELETE_ITEM_BY_ID, id);
    }

    @Override
    public void deleteAllItems() {

    }

}
