package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.model.Request;

import java.util.List;

public interface RequestService {

    RequestDto addRequest(RequestDto requestDto, int userId);

    List<Request> getAllOwnerRequests(int ownerId);

    List<RequestDto> getAllRequests(int userId, Integer from, Integer size);

    Request getRequest(int userId, int id);
}
