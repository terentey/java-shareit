package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto save(ItemDto itemDto, long userId);

    CommentDto saveComment(CommentDto commentDto, long itemId, long userId);

    ItemDto update(ItemDto itemDto, long id, long userId);

    ItemDto findById(long id, long userId);

    List<ItemDto> findAll(long userId);

    List<ItemDto> search(long userId, String text);
}
