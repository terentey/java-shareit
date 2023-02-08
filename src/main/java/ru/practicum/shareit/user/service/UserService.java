package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto save(UserDto userDto);

    UserDto update(UserDto userDto, long id);

    List<UserDto> findAll();

    UserDto findById(long id);

    void delete(long id);
}
