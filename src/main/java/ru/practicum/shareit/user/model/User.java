package ru.practicum.shareit.user.model;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * TODO Sprint add-controllers.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
//    @Column(nullable = false, length = 64)
    private String name;
    @Email(message = "Неверно указан email")
    @NotBlank(message = "Неверные данные: Email пуст или содержит только пробелы")
    private String email;
    @Enumerated(EnumType.STRING)
    @Transient
    private UserState state;

    public enum UserState {
        ACTIVE, BLOCKED, DELETED;
    }
}
