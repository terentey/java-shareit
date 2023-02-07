package ru.practicum.shareit.item.dao;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.IncorrectIdException;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class InMemoryItemRepository implements ItemRepository {
    private final Map<Long, List<Item>> items = new HashMap<>();
    private long count;

    @Override
    public Item save(Item item, long userId) {
        item.setId(++count);
        if (items.containsKey(userId)) items.get(userId).add(item);
        else items.put(userId, List.of(item));
        return item;
    }

    @Override
    public Item update(Map<String, String> patch, long id, long userId) {
        Item item = findById(id, userId).orElseThrow(IncorrectIdException::new);
        if (patch.containsKey("name")) item.setName(patch.get("name"));
        if (patch.containsKey("description")) item.setDescription(patch.get("description"));
        if (patch.containsKey("available")) item.setAvailable(Boolean.valueOf(patch.get("available")));
        return item;
    }

    @Override
    public Optional<Item> findById(long id, long userId) {
        if (!items.containsKey(userId)) throw new IncorrectIdException();
        return items.get(userId).stream().filter(i -> i.getId() == id).findFirst();
    }

    @Override
    public Optional<Item> findById(long id) {
        return items.values().stream()
                .map(list -> list.stream().filter(i -> i.getId() == id).findFirst()).findFirst().get();
    }

    @Override
    public List<Item> findAll(long userId) {
        return items.get(userId);
    }

    @Override
    public List<Item> search(String text) {
        if (text.isEmpty()) return new ArrayList<>();
        final String search = text.toLowerCase();
        final String capitalizedSearch = search.substring(0, 1).toUpperCase() + search.substring(1);
        return items.values().stream().flatMap(Collection::stream)
                .filter(i -> i.getName().contains(search) || i.getName().contains(capitalizedSearch) ||
                        i.getDescription().contains(search) || i.getDescription().contains(capitalizedSearch) && i.getAvailable())
                .collect(Collectors.toList());
    }
}
