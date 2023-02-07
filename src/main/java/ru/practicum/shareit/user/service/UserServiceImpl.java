package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.IncorrectIdException;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    public UserDto save(User user) {
        return UserMapper.mapToUserDto(repository.save(user));
    }

    @Override
    public UserDto update(Map<String, String> patch, long id) {
        return UserMapper.mapToUserDto(repository.update(patch, id));
    }

    @Override
    public List<UserDto> findAll() {
        return repository.findAll().stream().map(UserMapper::mapToUserDto).collect(Collectors.toList());
    }

    @Override
    public UserDto findById(long id) {
        return UserMapper.mapToUserDto(repository.findById(id).orElseThrow(IncorrectIdException::new));
    }

    @Override
    public void delete(long id) {
        repository.delete(id);
    }
}
