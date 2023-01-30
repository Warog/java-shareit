package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable int id) {
        log.info("Получить данные пользователя с ID = {}", id);

        return userService.getUser(id);
    }

    @PostMapping
    public UserDto createUser(@RequestBody User user) {
        log.info("Создать пользователя с данными: {}", user);


        return userService.addUser(user);
    }

    @PatchMapping("/{id}")
    public UserDto updateUser(@PathVariable int id, @RequestBody UserDto userDto) {
        userDto.setId(id);
        log.info("Обновить пользователя. Данные: {}", userDto);

        return userService.updateUser(UserMapper.toUser(userDto));
    }

    @GetMapping
    public List<User> getAllUsers() {
        log.info("Вывод всех пользователей");

        return userService.allUsers();
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable int id) {
        log.info("Удалить пользователя с ID = {}", id);

        userService.deleteUser(id);
    }


}
