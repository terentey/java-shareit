package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exception.IncorrectBookerId;
import ru.practicum.shareit.exception.IncorrectIdException;
import ru.practicum.shareit.item.dao.CommentRepository;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final UserRepository userRepo;
    private final CommentRepository commentRepo;
    private final ItemRepository repository;

    @Transactional
    @Override
    public ItemDto save(ItemDto itemDto, long userId) {
        return ItemMapper
                .mapToItemDto(repository
                        .saveAndFlush(ItemMapper
                                .mapToItem(itemDto, userRepo.findById(userId).orElseThrow(IncorrectIdException::new))));
    }

    @Transactional
    @Override
    public CommentDto saveComment(CommentDto commentDto, long itemId, long userId) {
        Item item = repository.findById(itemId).orElseThrow(IncorrectIdException::new);
        if (userId == item.getUser().getId()) throw new IncorrectIdException();
        if (item.getBookings().isEmpty() || item.getBookings().stream()
                .noneMatch(b -> b.getUser().getId() == userId && b.getStatus() != Status.REJECTED
                        && b.getStart().isBefore(LocalDateTime.now()))) {
            throw new IncorrectBookerId();
        }
        return CommentMapper.mapToCommentDto(commentRepo.saveAndFlush(CommentMapper.mapToComment(commentDto, item)));
    }

    @Transactional
    @Override
    public ItemDto update(ItemDto itemDto, long id, long userId) {
        Item item = repository.findById(id).orElseThrow(IncorrectIdException::new);
        if (userId != item.getUser().getId()) throw new IncorrectIdException();
        if (itemDto.getName() != null && !itemDto.getName().isBlank()) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null && !itemDto.getDescription().isBlank()) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }
        return ItemMapper.mapToItemDto(repository.saveAndFlush(item));
    }

    @Transactional
    @Override
    public ItemDto findById(long id, long userId) {
        Item item = repository.findById(id).orElseThrow(IncorrectIdException::new);
        ItemDto itemDto;
        if (item.getUser().getId() == userId) {
            itemDto = ItemMapper.mapToItemDtoByOwner(item);
        } else {
            itemDto = ItemMapper.mapToItemDto(item);
        }
        return itemDto;
    }

    @Transactional
    @Override
    public List<ItemDto> findAll(long userId) {
        return ItemMapper.mapToItemDto(repository.findItemsByUserId(userId));
    }

    @Transactional
    @Override
    public List<ItemDto> search(long userId, String text) {
        if (text.isBlank()) {
            return Collections.emptyList();
        }
        return ItemMapper.mapToItemDto(repository
                .findItemsByAvailableTrueAndDescriptionContainingIgnoreCaseOrNameContainingIgnoreCase(text, text));
    }
}
