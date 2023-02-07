package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Map;

public interface UserService {
    UserDto save(User user);

    UserDto update(Map<String, String> patch, long id);

    List<UserDto> findAll();

    UserDto findById(long id);

    void delete(long id);
}
