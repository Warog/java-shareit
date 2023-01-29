package ru.practicum.shareit.exception;

public class CommentDeniedException extends RuntimeException {
    public CommentDeniedException(String message) {
        super(message);
    }

}
