package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDtoRequest;
import ru.practicum.shareit.item.dto.CommentDtoResponse;
import ru.practicum.shareit.item.dto.ItemDtoRequest;
import ru.practicum.shareit.item.dto.ItemDtoResponse;

import java.util.List;

public interface ItemService {
    ItemDtoResponse save(ItemDtoRequest itemDto, long userId);

    CommentDtoResponse saveComment(CommentDtoRequest commentDto, long itemId, long userId);

    ItemDtoResponse update(ItemDtoRequest itemDto, long id, long userId);

    ItemDtoResponse findById(long id, long userId);

    List<ItemDtoResponse> findAll(long userId, int from, Integer size);

    List<ItemDtoResponse> search(long userId, String text, int from, Integer size);
}
