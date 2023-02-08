package ru.practicum.shareit.item.dao;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.IncorrectIdException;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class InMemoryItemRepository implements ItemRepository {
    private final Map<Long, List<Item>> userItemIndex = new HashMap<>();
    private long count;

    @Override
    public Item save(Item item, long userId) {
        item.setId(++count);
        final List<Item> items = userItemIndex.computeIfAbsent(userId, k -> new ArrayList<>());
        items.add(item);
        return item;
    }

    @Override
    public Item update(Item item, long id, long userId) {
        Item updatedItem = findById(id, userId).orElseThrow(IncorrectIdException::new);
        if (item.getName() != null && !item.getName().isBlank()) updatedItem.setName(item.getName());
        if (item.getDescription() != null && !item.getDescription().isBlank())
            updatedItem.setDescription(item.getDescription());
        if (item.getAvailable() != null) updatedItem.setAvailable(item.getAvailable());
        return updatedItem;
    }

    @Override
    public Optional<Item> findById(long id, long userId) {
        if (!userItemIndex.containsKey(userId)) throw new IncorrectIdException();
        return userItemIndex.get(userId).stream().filter(i -> i.getId() == id).findFirst();
    }

    @Override
    public Optional<Item> findById(long id) {
        return userItemIndex.values().stream()
                .map(list -> list.stream().filter(i -> i.getId() == id).findFirst()).findFirst().get();
    }

    @Override
    public List<Item> findAll(long userId) {
        return userItemIndex.get(userId);
    }

    @Override
    public List<Item> search(String text) {
        final String search = text.toLowerCase();
        final String capitalizedSearch = search.substring(0, 1).toUpperCase() + search.substring(1);
        return userItemIndex.values().stream().flatMap(Collection::stream)
                .filter(i -> i.getName().contains(search) || i.getName().contains(capitalizedSearch) ||
                        i.getDescription().contains(search) ||
                        i.getDescription().contains(capitalizedSearch) && i.getAvailable())
                .collect(Collectors.toList());
    }
}
