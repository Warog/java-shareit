package ru.practicum.shareit.item.model;

import lombok.Builder;
import lombok.Data;

/**
 * TODO Sprint add-controllers.
 */

@Data
@Builder
public class Item {
    Integer id;
    // краткое название
    String name;
    // развёрнутое описание
    String description;
    // статус о том, доступна или нет вещь для аренды
    Boolean available;
    // владелец вещи
    Integer owner;
    // если вещь была создана по запросу другого пользователя, то в этом
    //поле будет храниться ссылка на соответствующий запрос
    Integer request;
}
