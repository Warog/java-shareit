package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.service.UserServiceImpl;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
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
    public UserDto createUser(@Valid @RequestBody UserDto userDto) {
        log.info("Создать пользователя с данными: {}", userDto);


        return userService.addUser(userDto);
    }

    @PatchMapping("/{id}")
    public UserDto updateUser(@PathVariable int id, @Valid @RequestBody UserDto userDto) {
        userDto.setId(id);
        log.info("Обновить пользователя. Данные: {}", userDto);

        return userService.updateUser(userDto);
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        log.info("Вывод всех пользователей");

        return userService.allUsers();
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable int id) {
        log.info("Удалить пользователя с ID = {}", id);

        userService.deleteUser(id);
    }


}
