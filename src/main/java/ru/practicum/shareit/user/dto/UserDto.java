package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.validation.constraints.Email;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    Integer id;
    String name;
    @Email(message = "Неверно указан email")
//    @NotBlank(message = "Неверные данные: Email пуст или содержит только пробелы")
    String email;
}
