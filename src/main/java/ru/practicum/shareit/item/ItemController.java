package ru.practicum.shareit.item;

import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.service.ItemServiceImpl;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    public ItemController(ItemServiceImpl itemServiceImpl) {
        this.itemService = itemServiceImpl;
    }

    @GetMapping("/{id}")
    public ItemDto getItem(@PathVariable int id) {

        return itemService.getItem(id);
    }

    @PostMapping
    public ItemDto createItem(@RequestHeader("X-Sharer-User-Id") int id, @RequestBody ItemDto itemDto) {

        itemDto.setId(id);

        return itemService.addItem(itemDto);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItem(@RequestParam String text) {
        return itemService.searchItem(text);
    }
}
