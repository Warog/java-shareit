package ru.practicum.shareit.item.service;

import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    ItemDto getItem(int id, Integer userId);

    ItemDto addItem(Integer ownerId, ItemDto itemDto);

    ItemDto updateItem(Integer ownerId, ItemDto itemDto);

    List<Item> searchItem(String description);

    List<ItemDto> allOwnerItems(int ownerId);

    CommentDto addComment(int authorId, int itemId, Comment comment);
}
