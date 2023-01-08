package ru.practicum.shareit.comment.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */

@Data
@Builder
public class CommentDto {
    private Integer id;
    private String text;
    private Integer itemId;
    private String authorName;
    private LocalDateTime created;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CommentDto)) return false;
        return id != null && id.equals(((CommentDto) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}


