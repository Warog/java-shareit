package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {
    UserDto getUser(int id);

    UserDto addUser(User user);

    UserDto updateUser(User user);

    List<User> allUsers();

    void deleteUser(int id);

}
