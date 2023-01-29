package ru.practicum.shareit.comment.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentDto {
    Integer id;
    String text;
    Integer itemId;
    String authorName;
    LocalDateTime created;
}