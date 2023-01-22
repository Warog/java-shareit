package ru.practicum.shareit.request.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.IncorrectParamInRequestException;
import ru.practicum.shareit.exception.RequestNotFoundException;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.mapper.RequestMapper;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class RequestServiceImpl implements RequestService {
    UserService userService;
    RequestRepository requestRepository;

    public RequestServiceImpl(UserServiceImpl userServiceImpl, RequestRepository requestRepository) {
        this.userService = userServiceImpl;
        this.requestRepository = requestRepository;
    }

    @Transactional
    @Override
    public RequestDto addRequest(RequestDto requestDto, int userId) {
        userService.getUser(userId); // TODO usernotfound???

        if (requestDto.getDescription() == null)
            throw new IncorrectParamInRequestException("description = null");

        Request request = RequestMapper.toRequest(requestDto);
        request.setRequestor(userId);
        request.setCreated(LocalDateTime.now());

        return RequestMapper.toRequestDto(requestRepository.save(request));

    }

    @Transactional
    @Override
    public List<Request> getAllOwnerRequests(int ownerId) {
        userService.getUser(ownerId);

        return requestRepository.getAllByRequestorOrderByCreatedDesc(ownerId);

    }

    @Transactional
    @Override
    public List<RequestDto> getAllRequests(int userId, Integer from, Integer size) {
        if (from == null || size == null) {
            return requestRepository.getRequestsByRequestorNot(userId).stream()
                    .map(RequestMapper::toRequestDto).collect(Collectors.toList());
        }

        return requestRepository.getRequestsByRequestorNot(userId, PageRequest.of(from, size,  Sort.by("created").descending())).stream()
                .map(RequestMapper::toRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public Request getRequest(int userId, int id) {
        userService.getUser(userId);
        Optional<Request> request = requestRepository.findById(id);
        if (request.isEmpty())
            throw new RequestNotFoundException(String.format("Запрос с id = %d не найден!", id));

        return request.get();
    }
}
