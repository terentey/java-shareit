package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.IncorrectIdException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository repository;

    @Override
    public ItemDto save(Item item, long userId) {
        return ItemMapper.mapToItemDto(repository.save(item, userId));
    }

    @Override
    public ItemDto update(Map<String, String> patch, long id, long userId) {
        return ItemMapper.mapToItemDto(repository.update(patch, id, userId));
    }

    @Override
    public ItemDto findById(long id, long userId) {
        return ItemMapper.mapToItemDto(repository.findById(id).orElseThrow(IncorrectIdException::new));
    }

    @Override
    public List<ItemDto> findAll(long userId) {
        return repository.findAll(userId).stream().map(ItemMapper::mapToItemDto).collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> search(long userId, String text) {
        return repository.search(text).stream().map(ItemMapper::mapToItemDto).collect(Collectors.toList());
    }
}
