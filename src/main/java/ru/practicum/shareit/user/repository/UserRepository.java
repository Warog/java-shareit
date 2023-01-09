package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserRepository {
    User getUser(int id);

    User addUser(User user);

    User updateUser(UserDto user);

    List<User> allUsers();

    void deleteUser(int id);

}
