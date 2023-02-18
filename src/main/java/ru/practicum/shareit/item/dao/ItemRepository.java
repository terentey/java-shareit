package ru.practicum.shareit.item.dao;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {
    Item save(Item item, long userId);

    Item update(Item item, long id, long userId);

    Optional<Item> findById(long id, long userId);

    Optional<Item> findById(long id);

    List<Item> findAll(long userId);

    List<Item> search(String text);
}
