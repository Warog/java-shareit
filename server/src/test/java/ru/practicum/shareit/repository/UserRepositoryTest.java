package ru.practicum.shareit.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.UserEmailEmptyException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.repository.UserRepositoryImpl;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@Transactional
@SpringBootTest(
        properties = "spring.datasource.url=jdbc:h2:file:./db/shareitTest"
)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ExtendWith(MockitoExtension.class)
public class UserRepositoryTest {
    UserRepository userRepository;
    @Mock
    EntityManager entityManager;

    @BeforeEach
    void setUp() {
        userRepository = new UserRepositoryImpl(entityManager);
    }

    @Test
    void addUser_emailEmpty() {
        User user = User.builder()
                .id(1)
                .name("A")
                .email(null)
                .build();

        Throwable exception = assertThrows(
                UserEmailEmptyException.class,
                () -> userRepository.addUser(user)
        );
        assertEquals("Не указан E-Mail", exception.getMessage());
    }

    @Test
    void getUser_userNotFound() {
        when(entityManager.find(any(), anyInt()))
                .thenReturn(null);

        Throwable exception = assertThrows(
                UserNotFoundException.class,
                () -> userRepository.getUser(1)
        );
        assertEquals("Пользователь с ID = 1 не найден!", exception.getMessage());
    }
}
