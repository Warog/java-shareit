package ru.practicum.shareit.integration;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql(scripts = {"file:src/test/java/resources/test_data.sql"})
@SpringBootTest(
        properties = "spring.datasource.url=jdbc:h2:file:./db/shareitTest",
        webEnvironment = SpringBootTest.WebEnvironment.NONE
)
public class ItemServiceIntegrationTest {
    private final ItemService itemService;

    private final EntityManager em;

    @Test
    void addItem() {
        int userId = 2;
        ItemDto item = ItemDto.builder()
                .name("testItem")
                .description("testDescription")
                .available(true)
                .build();

        ItemDto itemDto = itemService.addItem(userId, item);

        TypedQuery<Item> query = em.createQuery("SELECT i FROM Item i where i.owner = :userId", Item.class);
        Item itemResult = query.setParameter("userId", itemDto.getOwner()).getSingleResult();

        assertNotNull(itemResult);
        assertEquals(userId, itemResult.getOwner());
        assertEquals(itemDto.getName(), itemResult.getName());
        assertEquals(itemDto.getDescription(), itemResult.getDescription());
        assertTrue(itemResult.getAvailable());
    }

    @Test
    void addItemWithOutRequestId() {
        int userId = 2;
        ItemDto item = ItemDto.builder()
                .name("testItem")
                .description("testDescription")
                .available(true)
                .requestId(1)
                .build();

        ItemDto itemDto = itemService.addItem(userId, item);

        TypedQuery<Item> query = em.createQuery("SELECT i FROM Item i where i.owner = :userId", Item.class);
        Item itemResult = query.setParameter("userId", itemDto.getOwner()).getSingleResult();

        Query reqItemQuery = em.createNativeQuery("SELECT r.request_id, r.item_id FROM Request_Item r WHERE r.item_id = :itemId");
        List<Object[]> resultList = reqItemQuery.setParameter("itemId", itemResult.getId()).getResultList();

        assertNotNull(itemResult);
        assertEquals(userId, itemResult.getOwner());
        assertEquals(itemDto.getName(), itemResult.getName());
        assertEquals(itemDto.getDescription(), itemResult.getDescription());
        assertTrue(itemResult.getAvailable());

        assertNotNull(resultList);

        resultList.forEach(objects -> {
            Integer requestId = (Integer) objects[0];
            Integer itemId = (Integer) objects[1];

            assertEquals(itemResult.getId(), itemId);
            assertEquals(item.getRequestId(), requestId);
        });

    }

    @Test
    void updateItem() {
        int userId = 1;

        TypedQuery<Item> query = em.createQuery("SELECT i FROM Item i where i.owner = :userId", Item.class);
        Item currentItem = query.setParameter("userId", userId).getSingleResult();

        ItemDto updatedItem = ItemDto.builder()
                .id(1)
                .name("testItemUpdated")
                .description("testDescriptionUpdated")
                .available(false)
                .owner(userId)
                .build();

        assertNotEquals(updatedItem.getName(), currentItem.getName());
        assertNotEquals(updatedItem.getAvailable(), currentItem.getAvailable());
        assertNotEquals(updatedItem.getDescription(), currentItem.getDescription());

        ItemDto itemDtoUpdate = itemService.updateItem(userId, updatedItem);

        Item itemAfterUpdate = query.setParameter("userId", userId).getSingleResult();

        assertNotNull(itemAfterUpdate);
        assertEquals(userId, itemAfterUpdate.getOwner());
        assertEquals(itemDtoUpdate.getName(), itemAfterUpdate.getName());
        assertEquals(itemDtoUpdate.getDescription(), itemAfterUpdate.getDescription());
        assertFalse(itemAfterUpdate.getAvailable());
    }
    @Test
    void searchItem() {
        List<Item> dreL = itemService.searchItem("dreL");

        assertNotNull(dreL);
        assertEquals(1, dreL.size());
        assertTrue(dreL.get(0).getDescription().contains("dreL".toLowerCase()));

    }
}
