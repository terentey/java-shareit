package ru.practicum.shareit.util;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

public class ItemTest {
    public static Item getNewItem(Long id,
                                  String name,
                                  String description,
                                  Boolean available,
                                  User user,
                                  ItemRequest itemRequest) {
        Item item = getNewItem(id, name, description, available, user);
        item.setItemRequest(itemRequest);
        return item;
    }

    public static Item getNewItem(Long id,
                                  String name,
                                  String description,
                                  Boolean available,
                                  User user) {
        Item item = getNewItem(name, description, available, user);
        item.setId(id);
        return item;
    }

    public static Item getNewItem(String name,
                                  String description,
                                  Boolean available,
                                  User user) {
        Item item = new Item();
        item.setName(name);
        item.setDescription(description);
        item.setAvailable(available);
        item.setUser(user);
        return item;
    }
}
