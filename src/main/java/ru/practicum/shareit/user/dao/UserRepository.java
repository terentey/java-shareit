package ru.practicum.shareit.user.dao;

import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserRepository {
    User save(User user);

    User update(Map<String, String> patch, long id);

    List<User> findAll();

    Optional<User> findById(long id);

    void delete(long id);
}
