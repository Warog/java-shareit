package ru.practicum.shareit.user.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

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
    @Email(message = "Неверно указан email")
    @NotBlank(message = "Неверные данные: Email пуст или содержит только пробелы")
    String email;
}
