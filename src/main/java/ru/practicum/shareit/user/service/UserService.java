package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto getUser(int id);

    UserDto addUser(UserDto userDto);

    UserDto updateUser(UserDto userDto);

    List<UserDto> allUsers();

    void deleteUser(int id);

}
