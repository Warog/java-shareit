package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.service.ItemServiceImpl;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */

@Slf4j
@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    public ItemController(ItemServiceImpl itemServiceImpl) {
        this.itemService = itemServiceImpl;
    }

    @GetMapping("/{id}")
    public ItemDto getItem(@PathVariable int id, @RequestHeader("X-Sharer-User-Id") Integer userId) {
        log.info("Получить предмет с ID = {}", id);

        return itemService.getItem(id, userId);
    }

    @PostMapping
    public ItemDto createItem(@RequestHeader("X-Sharer-User-Id") Integer ownerId, @RequestBody ItemDto itemDto) {
        log.info("Создать предмет с данными: {}", itemDto);

        return itemService.addItem(ownerId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") int ownerId, @PathVariable int itemId, @RequestBody ItemDto itemDto) {
        log.info("Изменить данные предмета с ID = {}. \n OWNER_ID = {}. \n Новые данные предмета: {}", itemId, ownerId, itemDto);

//        itemDto.setOwner(ownerId);
        itemDto.setId(itemId);

        return itemService.updateItem(ownerId, itemDto);
    }


    @GetMapping("/search")
    public List<Item> searchItem(@RequestParam String text) {
        log.info("Найти предмет по описанию: {}", text);

        return itemService.searchItem(text);
    }

    @GetMapping
    public List<ItemDto> getAllOwnersItems(@RequestHeader("X-Sharer-User-Id") int ownerId) {
        log.info("Получить список предметов владельца с ID = {}", ownerId);

        return itemService.allOwnerItems(ownerId);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(@RequestHeader("X-Sharer-User-Id") int authorId, @PathVariable int itemId, @RequestBody @Valid Comment comment) {
        log.info("Добавить комментарий authorId = {}, itemId = {}, comment = {}", authorId, itemId, comment);

        return itemService.addComment(authorId, itemId, comment);

    }

}
