package ru.practicum.shareit.util;

import ru.practicum.shareit.user.model.User;

public class UserTest {
    public static User getNewUser(String name, String email) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        return user;
    }

    public static User getNewUser(Long id, String name, String email) {
        User user = getNewUser(name, email);
        user.setId(id);
        return user;
    }
}
