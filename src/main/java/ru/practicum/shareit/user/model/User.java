package ru.practicum.shareit.user.model;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * TODO Sprint add-controllers.
 */

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    private Integer id;
//    @Column(nullable = false, length = 64)
    private String name;
    @Email(message = "Неверно указан email")
    @NotBlank(message = "Неверные данные: Email пуст или содержит только пробелы")
    private String email;
    @Enumerated(EnumType.STRING)
    private UserState state;

    public enum UserState {
        ACTIVE, BLOCKED, DELETED;
    }
}
