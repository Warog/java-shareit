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
    private Integer id;
    private String name;
    @Email(message = "Неверно указан email")
    @NotBlank(message = "Неверные данные: Email пуст или содержит только пробелы")
    private String email;
}
