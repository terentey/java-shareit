package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.IncorrectIdException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final UserService userService;
    private final ItemRepository repository;

    @Override
    public ItemDto save(ItemDto itemDto, long userId) {
        userService.findById(userId);
        return ItemMapper.mapToItemDto(repository.save(ItemMapper.mapToItem(itemDto), userId));
    }

    @Override
    public ItemDto update(ItemDto itemDto, long id, long userId) {
        userService.findById(userId);
        return ItemMapper.mapToItemDto(repository.update(ItemMapper.mapToItem(itemDto), id, userId));
    }

    @Override
    public ItemDto findById(long id, long userId) {
        return ItemMapper.mapToItemDto(repository.findById(id).orElseThrow(IncorrectIdException::new));
    }

    @Override
    public List<ItemDto> findAll(long userId) {
        return ItemMapper.mapToItemDto(repository.findAll(userId));
    }

    @Override
    public List<ItemDto> search(long userId, String text) {
        if (text.isBlank()) {
            return Collections.emptyList();
        }
        return repository.search(text).stream().map(ItemMapper::mapToItemDto).collect(Collectors.toList());
    }
}
