package ru.practicum.shareit.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.mapper.RequestMapper;
import ru.practicum.shareit.request.service.RequestService;
import ru.practicum.shareit.request.service.RequestServiceImpl;

import java.util.List;
import java.util.stream.Collectors;

/**
 * TODO Sprint add-item-requests.
 */

@Slf4j
@RestController
@RequestMapping(path = "/requests")
public class RequestController {

    private final RequestService requestService;

    public RequestController(RequestServiceImpl requestServiceImpl) {
        this.requestService = requestServiceImpl;
    }

    @PostMapping
    public RequestDto addRequest(@RequestHeader(name = "X-Sharer-User-Id") int userId, @RequestBody RequestDto requestDto) {
        log.info("Добавить новый запрос: {}", requestDto);

        return requestService.addRequest(requestDto, userId);

    }

    @GetMapping
    public List<RequestDto> getRequests(@RequestHeader(name = "X-Sharer-User-Id") int userId) {
        log.info("Получить саисок своих запросов. userId = {}", userId);

        return requestService.getAllOwnerRequests(userId).stream()
                .map(RequestMapper::toRequestDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/all")
    public List<RequestDto> getRequestPage(@RequestHeader(name = "X-Sharer-User-Id") int userId, @RequestParam(name = "from", required = false) Integer from, @RequestParam(name = "size", required = false) Integer size) {
        log.info("Получить запросы в виде страницы [{}, {}]", from, size);

        return requestService.getAllRequests(userId);
    }

    @GetMapping("/{id}")
    public RequestDto getRequest(@PathVariable(name = "id") int id) {
        log.info("Получить запрос по id = {}", id);

        return RequestMapper.toRequestDto(requestService.getRequest(id));
    }
}
