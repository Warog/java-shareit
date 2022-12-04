package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {
    UserDto getUser(int id);

    User addUser(User user);

    User updateUser(User user);

    List<User> allUsers();

    void deleteAllUsers();
}
