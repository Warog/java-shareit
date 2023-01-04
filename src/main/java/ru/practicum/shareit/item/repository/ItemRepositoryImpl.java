package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.exception.UserNotOwnerException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.validate.ValidateItem;
import ru.practicum.shareit.user.model.User;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.util.List;

@Repository
public class ItemRepositoryImpl implements ItemRepository {
    private final EntityManager entityManager;

    public ItemRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Item getItem(int id) {
        Item item = entityManager.find(Item.class, id);
        if (item == null)
            throw new ItemNotFoundException("Объект не найден!");

        return item;

    }

    @Override
    public Item addItem(Integer ownerId, Item item) {
        User user = entityManager.find(User.class, ownerId);
        if (user == null)
            throw new UserNotFoundException("Владельца с указанным Id не существует!");

        item.setOwner(ownerId);

        ValidateItem.validateParamOnNull(item);
        ValidateItem.validateOnEmptyName(item);

        entityManager.persist(item);

        return getItem(item.getId());
    }

    @Override
    public Item updateItem(Integer ownerId, ItemDto itemDto) {
        if (!getItem(itemDto.getId()).getOwner().equals(ownerId))
            throw new UserNotOwnerException("Пользователь не является владельцем!");

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaUpdate<Item> cu = cb.createCriteriaUpdate(Item.class);
        Root<Item> root = cu.from(Item.class);

        if (itemDto.getName() != null)
            cu.set("name", itemDto.getName());
        if (itemDto.getDescription() != null)
            cu.set("description", itemDto.getDescription());
        if (itemDto.getAvailable() != null)
            cu.set("available", itemDto.getAvailable());

        cu.where(cb.equal(root.get("id"), itemDto.getId()));

        entityManager.createQuery(cu).executeUpdate();
        entityManager.clear();

        return getItem(itemDto.getId());
    }

    @Override
    public List<Item> searchItem(String description) {
        if (description.isBlank())
            return List.of();

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Item> cq = cb.createQuery(Item.class);
        Root<Item> root = cq.from(Item.class);
        cq.select(root).where(cb.like(cb.lower(root.get("description")), "%" + description.toLowerCase() + "%"), cb.equal(root.get("available"), true));

        return entityManager.createQuery(cq).getResultList();
    }

    @Override
    public List<Item> allItems() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Item> cr = cb.createQuery(Item.class);
        Root<Item> root = cr.from(Item.class);
        cr.select(root);

        return entityManager.createQuery(cr).getResultList();
    }

    @Override
    public List<Item> allOwnerItems(int ownerId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Item> cr = cb.createQuery(Item.class);
        Root<Item> root = cr.from(Item.class);
        cr.select(root).where(cb.equal(root.get("owner"), ownerId));

        return entityManager.createQuery(cr).getResultList();
    }

    @Override
    public void deleteItem(int id) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaDelete<Item> cd = cb.createCriteriaDelete(Item.class);
        Root<Item> root = cd.from(Item.class);
        cd.where(cb.equal(root.get("id"), id));

        entityManager.createQuery(cd).executeUpdate();
    }

    @Override
    public void deleteAllItems() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaDelete<Item> cd = cb.createCriteriaDelete(Item.class);
        cd.from(Item.class);

        entityManager.createQuery(cd).executeUpdate();
    }

    /*
    @Override
    public ItemDto getItem(int id) {

        return jdbcTemplate.queryForObject(SQL_GET_ITEM_BY_ID, new MapRowToItemDto(), id);
    }

    @Override
    public ItemDto addItem(Integer ownerId, ItemDto itemDto) {

        try {
            jdbcTemplate.queryForObject(SQL_FOUND_USER, Integer.class, ownerId);
        } catch (EmptyResultDataAccessException e) {
            throw new UserNotFoundException(String.format("Владелец с ID = %d не найден!", ownerId), e.getCause());
        }

        ValidateItem.validateParamOnNull(itemDto);
        ValidateItem.validateOnEmptyName(itemDto);

        jdbcTemplate.update(SQL_ADD_ITEM, itemDto.getName(), itemDto.getDescription(), itemDto.getAvailable(), itemDto.getOwner(), itemDto.getItemRequest());

        return jdbcTemplate.queryForObject(SQL_GET_ITEM_BY_ALL_PARAMETERS, new MapRowToItemDto(), itemDto.getName(), itemDto.getDescription(), itemDto.getAvailable());
    }

    @Override
    public ItemDto updateItem(Integer ownerId, ItemDto itemDto) {

        Optional<Integer> isOwnerOptional = Optional.ofNullable(jdbcTemplate.queryForObject(SQL_GET_OWNER_ITEM, Integer.class, ownerId, itemDto.getId()));

        if (isOwnerOptional.isPresent()) {
            if (isOwnerOptional.get() > 0)
                jdbcTemplate.update(SQL_UPDATE_ITEM_BY_ID, itemDto.getName(), itemDto.getDescription(), itemDto.getAvailable(), itemDto.getId());
            else
                throw new UserNotOwnerException("Пользователь не является владельцем!");
        } else {
            throw new IncorrectParamInRequestException("Указан не существующий параметр");
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
    */

}
