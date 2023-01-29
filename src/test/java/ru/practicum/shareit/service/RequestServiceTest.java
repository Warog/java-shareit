package ru.practicum.shareit.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.IncorrectParamInRequestException;
import ru.practicum.shareit.exception.RequestNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.request.service.RequestService;
import ru.practicum.shareit.request.service.RequestServiceImpl;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.when;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(
        properties = "spring.datasource.url=jdbc:h2:file:./db/shareitTest"
)
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

    @Test
    void addRequest_withoutItems() {
        when(userServiceImpl.getUser(anyInt()))
                .thenReturn(UserDto.builder()
                        .id(1)
                        .name("A")
                        .email("q@bmail.ru")
                        .build()
                );

        when(requestRepository.save(any()))
                .thenReturn(new Request(1, "Hammer", 2, LocalDateTime.now(), Collections.emptySet()));

        RequestDto requestDto = requestService.addRequest(new RequestDto(1, "Hammer", LocalDateTime.now(), Collections.emptySet()), 1);

        assertEquals(1, requestDto.getId());
        assertEquals("Hammer", requestDto.getDescription());
        assertEquals(0, requestDto.getItems().size());
    }

    @Test
    void addRequest_withItems() {
        Item item = Item.builder()
                .id(1)
                .owner(2)
                .name("Tork")
                .description("A")
                .available(true)
                .build();

        when(userServiceImpl.getUser(anyInt()))
                .thenReturn(UserDto.builder()
                        .id(1)
                        .name("A")
                        .email("q@bmail.ru")
                        .build()
                );

        when(requestRepository.save(any()))
                .thenReturn(new Request(1, "Hammer", 2, LocalDateTime.now(), Set.of(item)));

        RequestDto requestDto = requestService.addRequest(new RequestDto(1, "Hammer", LocalDateTime.now(), Collections.emptySet()), 1);

        assertEquals(1, requestDto.getId());
        assertEquals("Hammer", requestDto.getDescription());
        assertEquals(1, requestDto.getItems().size());
    }

    @Test
    void getAllOwnerRequests() {
        Request request = new Request(1, "Hammer", 1, LocalDateTime.now(), Collections.emptySet());

        when(userServiceImpl.getUser(anyInt()))
                .thenReturn(UserDto.builder()
                        .id(1)
                        .name("A")
                        .email("q@bmail.ru")
                        .build()
                );

        when(requestRepository.getAllByRequestorOrderByCreatedDesc(anyInt()))
                .thenReturn(List.of(request, request));

        List<Request> allOwnerRequests = requestService.getAllOwnerRequests(1);

        assertNotNull(allOwnerRequests);
        assertEquals(2, allOwnerRequests.size());
    }

    @Test
    void getAllRequests_fromIsNullSizeIsNull() {
        Request request = new Request(1, "Hammer", 1, LocalDateTime.now(), Collections.emptySet());

        when(requestRepository.getRequestsByRequestorNot(anyInt()))
                .thenReturn(List.of(request, request));

        List<RequestDto> allRequests = requestService.getAllRequests(1, null, null);

        assertNotNull(allRequests);
        assertEquals(2, allRequests.size());
    }

    @Test
    void getAllRequests_withFromAndSize() {
        Request request = new Request(1, "Hammer", 1, LocalDateTime.now(), Collections.emptySet());

        when(requestRepository.getRequestsByRequestorNot(anyInt(), any()))
                .thenReturn(new PageImpl<>(List.of(request, request)));

        List<RequestDto> allRequests = requestService.getAllRequests(1, 0, 2);

        assertNotNull(allRequests);
        assertEquals(2, allRequests.size());
    }

    @Test
    void getRequest() {
        Request request = new Request(1, "Hammer", 1, LocalDateTime.now(), Collections.emptySet());

        when(userServiceImpl.getUser(anyInt()))
                .thenReturn(UserDto.builder()
                        .id(1)
                        .name("A")
                        .email("q@bmail.ru")
                        .build()
                );

        when(requestRepository.findById(anyInt()))
                .thenReturn(Optional.of(request));

        Request requestResult = requestService.getRequest(1, 1);

        assertNotNull(requestResult);
        assertEquals(1, requestResult.getId());
        assertEquals("Hammer", requestResult.getDescription());
        assertEquals(1, requestResult.getRequestor());
    }
}
