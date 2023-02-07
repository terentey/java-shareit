package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Map;

public interface ItemService {
    ItemDto save(Item item, long userId);

    ItemDto update(Map<String, String> patch, long id, long userId);

    ItemDto findById(long id, long userId);

    List<ItemDto> findAll(long userId);

    List<ItemDto> search(long userId, String text);
}
