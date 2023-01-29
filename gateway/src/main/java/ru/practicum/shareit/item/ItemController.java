package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.exception.CommentDeniedException;
import ru.practicum.shareit.exception.IncorrectParamInRequestException;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private final ItemClient itemClient;

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItem(@Positive @PathVariable long itemId,
                                          @Positive @RequestHeader("X-Sharer-User-Id") Integer userId) {
        log.info("Get item itemId={}, userId={}", itemId, userId);
        return itemClient.getItem(userId, itemId);
    }

    @PostMapping
    public ResponseEntity<Object> addItem(@Positive @RequestHeader("X-Sharer-User-Id") long userId,
                                          @Valid @RequestBody ItemDto item) {
        log.info("create item item={}, userId={}", item, userId);
        if(item.getAvailable() == null) {
            throw new IncorrectParamInRequestException("Не указан параметр 'Available'");
        }
        if(item.getName().isBlank()) {
            throw new IncorrectParamInRequestException("Неверно указан параметр 'Name'");
        }
        if(item.getDescription() == null) {
            throw new IncorrectParamInRequestException("Неверно указан параметр 'Description'");
        }

        return itemClient.addItem(item, userId);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@Positive @RequestHeader("X-Sharer-User-Id") long userId,
                                             @Valid @RequestBody ItemDto item,
                                             @Positive @NotNull @PathVariable long itemId) {
        log.info("update item item={}, userId={}", item, userId);
        item.setId(itemId);

        return itemClient.updateItem(userId, item);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItem(@RequestParam String text) {
        log.info("search item text={}", text);
        if (text.isBlank()) {
            return new ResponseEntity<>(List.of(), HttpStatus.OK);
        }
        return itemClient.searchItem(text);
    }

    @GetMapping
    public ResponseEntity<Object> getAllOwnersItems(@Positive @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("getAllOwnersItems userId={}", userId);
        return itemClient.allOwnerItems(userId);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@Positive @RequestHeader("X-Sharer-User-Id") int userId,
                                                @NotNull @PathVariable int itemId,
                                                @Valid @RequestBody CommentDto commentDto) {
        log.info("add comment comment={}", commentDto);
        if (commentDto.getText().isEmpty()) {
            throw new CommentDeniedException("Неверно указан комментарий");
        }
        return itemClient.addComment(userId, itemId, commentDto);
    }
}
