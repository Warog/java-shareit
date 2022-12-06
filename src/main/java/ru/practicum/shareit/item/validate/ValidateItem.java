package ru.practicum.shareit.item.validate;

import ru.practicum.shareit.exception.IncorrectParamInRequestException;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Optional;

public class ValidateItem {
    public static ItemDto validateParamOnNull(Optional<ItemDto> itemDtoOptional) {

        if (itemDtoOptional.isPresent()) {
            ItemDto validateItemDto = itemDtoOptional.get();

            if (validateItemDto.getAvailable() == null || validateItemDto.getName() == null || validateItemDto.getDescription() == null) {
                throw new IncorrectParamInRequestException("Не указан один из параметров Item");
            }

            return validateItemDto;
        }

        return null;
    }

    public static ItemDto validateOnEmptyName(Optional<ItemDto> itemDtoOptional) {

        if (itemDtoOptional.isPresent()) {
            ItemDto validateItemDto = itemDtoOptional.get();

            if (validateItemDto.getName().isEmpty()) {
                throw new IncorrectParamInRequestException("Имя Item указано неверно!");
            }

            return validateItemDto;
        }

        return null;
    }
}
