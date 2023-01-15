package ru.practicum.shareit.item.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.comment.dto.CommentDto;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemDto {
    Integer id;
    String name;
    String description;
    Boolean available;
    Integer owner;
    Integer requestId;
    ItemBookingDto lastBooking;
    ItemBookingDto nextBooking;
    List<CommentDto> comments;
}
