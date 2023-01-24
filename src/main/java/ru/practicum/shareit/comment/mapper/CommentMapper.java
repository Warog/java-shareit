package ru.practicum.shareit.comment.mapper;

import org.jetbrains.annotations.NotNull;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.user.model.User;

public class CommentMapper {
    public static CommentDto toCommentDto(@NotNull Comment comment, User user) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .authorName(user.getName())
                .created(comment.getCreated())
                .build();
    }
}
