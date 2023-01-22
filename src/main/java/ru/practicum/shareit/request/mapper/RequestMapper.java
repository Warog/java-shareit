package ru.practicum.shareit.request.mapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.request.dto.ItemRequest;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.model.Request;

import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class RequestMapper {

    public static RequestDto toRequestDto(Request request) {
        if (request.getItems() != null)
            return RequestDto.builder()
                    .id(request.getId())
                    .description(request.getDescription())
                    .created(request.getCreated())
                    .items(
                            request.getItems().stream()
                                    .map(item -> new ItemRequest(item.getId(), item.getName(), item.getDescription(), item.getAvailable(), request.getId(), item.getOwner()))
                                    .collect(Collectors.toSet()))
                    .build();

        return RequestDto.builder()
                .id(request.getId())
                .description(request.getDescription())
                .created(request.getCreated())
                .items(null)
                .build();
    }

    public static Request toRequest(RequestDto requestDto) {
        return Request.builder()
                .id(requestDto.getId())
                .description(requestDto.getDescription())
                .created(requestDto.getCreated())
                .build();

    }

}
