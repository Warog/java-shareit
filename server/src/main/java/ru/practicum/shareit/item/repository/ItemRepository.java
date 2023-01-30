package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Integer> {

    long countItemById(int id);

    List<Item> searchAllByDescriptionContainingIgnoreCaseAndAvailableTrue(String description);

    List<Item> findAllByOwnerOrderByIdAsc(int ownerId);

    @Modifying
    @Query(value = "INSERT INTO REQUEST_ITEM (request_id, item_id) VALUES ( :request_id, :item_id)",
            nativeQuery = true)
    void addItemWithRequest(@Param("request_id") Integer requestId, @Param("item_id") Integer itemId);
}
