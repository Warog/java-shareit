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
import ru.practicum.shareit.exception.IncorrectParamInRequestException;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.exception.UserNotOwnerException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.repository.ItemRepositoryImpl;
import ru.practicum.shareit.user.model.User;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@Transactional
@SpringBootTest(
        properties = "spring.datasource.url=jdbc:h2:file:./db/shareitTest"
)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ExtendWith(MockitoExtension.class)
public class ItemRepositoryTest {

    ItemRepository itemRepository;
    @Mock
    EntityManager entityManager;

    @BeforeEach
    void setUp() {
        itemRepository = new ItemRepositoryImpl(entityManager);
    }

    @Test
    void getItem_itemNotFound() {
        when(entityManager.find(any(), anyInt()))
                .thenReturn(null);

        Throwable exception = assertThrows(
                ItemNotFoundException.class,
                () -> itemRepository.getItem(1)
        );
        assertEquals("Объект не найден!", exception.getMessage());
    }

    @Test
    void addItem_nullableFields() {
        when(entityManager.find(any(), anyInt()))
                .thenReturn(new User());

        Throwable exception = assertThrows(
                IncorrectParamInRequestException.class,
                () -> itemRepository.addItem(1, new Item(1, null, null, null, 1, Collections.emptySet()))
        );
        assertEquals("Не указан один из параметров Item", exception.getMessage());
    }

    @Test
    void addItem_emptyName() {
        when(entityManager.find(any(), anyInt()))
                .thenReturn(new User());

        Throwable exception = assertThrows(
                IncorrectParamInRequestException.class,
                () -> itemRepository.addItem(1, new Item(1, "", "", true, 1, Collections.emptySet()))
        );
        assertEquals("Имя Item указано неверно!", exception.getMessage());
    }

    @Test
    void addItem_userNotFound() {
        when(entityManager.find(any(), anyInt()))
                .thenReturn(null);

        Throwable exception = assertThrows(
                UserNotFoundException.class,
                () -> itemRepository.addItem(1, new Item())
        );
        assertEquals("Пользователя с указанным Id не существует!", exception.getMessage());
    }

    @Test
    void updateItem_userNotBeOwner() {
        when(entityManager.find(any(), anyInt()))
                .thenReturn(new Item(1, "A", "B", true, 1, Collections.emptySet()));

        Throwable exception = assertThrows(
                UserNotOwnerException.class,
                () -> itemRepository.updateItem(2, ItemDto.builder()
                        .id(1)
                        .owner(1)
                        .build()
                )
        );
        assertEquals("Пользователь не является владельцем!", exception.getMessage());
    }

    @Test
    void searchItem_whenDescriptionIsBlank() {
        List<Item> items = itemRepository.searchItem("");

        assertEquals(0, items.size());
    }
}
