package ru.practicum.shareit.request.model;

import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
public class ItemRequest {
    Integer id;
    // ткст запроса, содержащий описание требуемой вещи
    String description;
    // пользователь, создавший запрос
    User requestor;
    // дата и время создания запроса
    LocalDateTime created;
}
