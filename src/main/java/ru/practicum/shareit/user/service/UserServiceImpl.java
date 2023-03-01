package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.IncorrectIdException;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Transactional
    @Override
    public UserDto save(UserDto userDto) {
        return UserMapper.mapToUserDto(repository.save(UserMapper.mapToUser(userDto)));
    }

    @Transactional
    @Override
    public UserDto update(UserDto userDto, long id) {
        User user = repository.findById(id).orElseThrow(IncorrectIdException::new);
        if (userDto.getName() != null && !userDto.getName().isBlank()) {
            user.setName(userDto.getName());
        }
        if (userDto.getEmail() != null && !userDto.getEmail().isBlank()) {
            user.setEmail(userDto.getEmail());
        }
        return UserMapper.mapToUserDto(repository.saveAndFlush(user));
    }

    @Transactional
    @Override
    public List<UserDto> findAll() {
        return UserMapper.mapToUserDto(repository.findAll());
    }

    @Transactional
    @Override
    public UserDto findById(long id) {
        return UserMapper.mapToUserDto(repository.findById(id).orElseThrow(IncorrectIdException::new));
    }

    @Transactional
    @Override
    public void delete(long id) {
        repository.deleteById(id);
    }
}
