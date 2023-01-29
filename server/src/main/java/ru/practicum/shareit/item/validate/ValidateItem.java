package ru.practicum.shareit.item.validate;

import ru.practicum.shareit.exception.IncorrectParamInRequestException;
import ru.practicum.shareit.item.model.Item;

public class ValidateItem {
    public static void validateParamOnNull(Item item) {

        if (item.getAvailable() == null || item.getName() == null || item.getDescription() == null) {
            throw new IncorrectParamInRequestException("Не указан один из параметров Item");
        }

    }

    public static void validateOnEmptyName(Item item) {
        if (item.getName().isEmpty()) {
            throw new IncorrectParamInRequestException("Имя Item указано неверно!");
        }
    }
}
