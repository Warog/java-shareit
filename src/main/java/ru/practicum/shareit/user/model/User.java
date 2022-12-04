package ru.practicum.shareit.user.model;

import lombok.Builder;
import lombok.Data;

/**
 * TODO Sprint add-controllers.
 */

@Data
@Builder
public class User {
    Integer id;
    // имя или логин пользователя
    String name;
     /*адрес электронной почты (учтите, что два пользователя не могут
     иметь одинаковый адрес электронной почты)*/
    String email;
}
