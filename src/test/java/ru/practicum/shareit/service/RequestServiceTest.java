package ru.practicum.shareit.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.IncorrectParamInRequestException;
import ru.practicum.shareit.exception.RequestNotFoundException;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.request.service.RequestService;
import ru.practicum.shareit.request.service.RequestServiceImpl;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RequestServiceTest {
    RequestService requestService;

    @Mock
    UserServiceImpl userServiceImpl;
    @Mock
    RequestRepository requestRepository;

    @BeforeEach
    void setUp() {
        requestService = new RequestServiceImpl(userServiceImpl, requestRepository);
    }

    @Test
    void addRequestWithoutDescription() {
        RequestDto requestDto = RequestDto.builder()
                .id(1)
                .build();

        when(userServiceImpl.getUser(anyInt()))
                .thenReturn(UserDto.builder()
                        .id(1)
                        .name("A")
                        .email("q@bmail.ru")
                        .build()
                );

        Throwable exception = assertThrows(
                IncorrectParamInRequestException.class,
                () -> requestService.addRequest(requestDto, 1)
        );
        assertEquals("description = null", exception.getMessage());
    }

    @Test
    void getAllOwnerRequestsWithoutUser() {
        when(userServiceImpl.getUser(anyInt()))
                .thenReturn(UserDto.builder()
                        .id(1)
                        .name("A")
                        .email("q@bmail.ru")
                        .build()
                );

        when(requestRepository.findById(anyInt()))
                .thenReturn(Optional.empty());

        Throwable exception = assertThrows(
                RequestNotFoundException.class,
                () -> requestService.getRequest(1, 2)
        );
        assertEquals("Запрос с id = 2 не найден!", exception.getMessage());
    }
}
