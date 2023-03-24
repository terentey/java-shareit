package ru.practicum.shareit.util;

import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

public class RequestTest {
    public static ItemRequest getNewRequest(Long id, String description, LocalDateTime created, User user) {
        ItemRequest request = getNewRequest(description, created, user);
        request.setId(id);
        return request;
    }

    public static ItemRequest getNewRequest(String description, LocalDateTime created, User user) {
        ItemRequest request = new ItemRequest();
        request.setDescription(description);
        request.setCreated(created);
        request.setUser(user);
        return request;
    }
}
