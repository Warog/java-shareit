package ru.practicum.shareit.user.mapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.sql.ResultSet;

@Data
@AllArgsConstructor
public class UserMapper {

    public static UserDto toUserDto(ResultSet rs, int rowNum) {
        return UserDto.builder()
                .id(1)
                .email("")
                .name("")
                .build();
    }

    public static User toUser() {
        return User.builder()
                .id(1)
                .email("")
                .name("")
                .build();
    }

}
