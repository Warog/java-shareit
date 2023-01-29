package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.UserEmailEmptyException;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {
    private final UserClient userClient;

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUserById(@Positive @PathVariable long userId) {
        log.info("Get user userId={}", userId);
        return userClient.getUser(userId);
    }

    @PostMapping
    public ResponseEntity<Object> createUser(@Valid @RequestBody UserDto user) {
        log.info("Создать пользователя с данными: {}", user);
        if (user.getEmail() == null) {
            throw new UserEmailEmptyException("E-Mail not found");
        }

        return userClient.addUser(user);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@Positive @PathVariable long userId, @Valid @RequestBody UserDto user) {
        log.info("Обновить пользователя с данными: {}", user);

        return userClient.updateUser(userId, user);
    }

    @GetMapping
    public ResponseEntity<Object> getAllUsers() {
        log.info("Вывод всех пользователей");

        return userClient.allUsers();
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@Positive @PathVariable int id) {
        log.info("Удалить пользователя с ID = {}", id);

        userClient.deleteUser(id);
    }
}
