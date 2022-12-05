package ru.practicum.shareit.exception;

public class UserEmailEmptyException extends RuntimeException {
    public UserEmailEmptyException(String message) {
        super(message);
    }
}
