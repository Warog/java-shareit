package ru.practicum.shareit.item.validate;

import ru.practicum.shareit.exception.IncorrectParamInRequestException;
import ru.practicum.shareit.item.dto.ItemDto;

public class ValidateItem {
    public static void validateParamOnNull(ItemDto itemDto) {

        if (itemDto.getAvailable() == null || itemDto.getName() == null || itemDto.getDescription() == null) {
            throw new IncorrectParamInRequestException("Не указан один из параметров Item");
        }

    }

    public static void validateOnEmptyName(ItemDto itemDto) {


        if (itemDto.getName().isEmpty()) {
            throw new IncorrectParamInRequestException("Имя Item указано неверно!");
        }
    }
}
