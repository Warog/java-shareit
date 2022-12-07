package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserRepository {
    UserDto getUser(int id);

    UserDto addUser(UserDto userDto);

    UserDto updateUser(UserDto userDto);

    List<UserDto> allUsers();

    void deleteUser(int id);

}
