package ru.practicum.shareit.exception;

public class IncorrectParamInRequestException extends RuntimeException{
    public IncorrectParamInRequestException(String message) {
        super(message);
    }
}
