package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

/**
 * TODO Sprint add-controllers.
 */

@Data
@Builder
public class ItemDto {
    Integer id;
    String name;
    String description;
    Boolean available;
    User owner;
    ItemRequest request;
}
