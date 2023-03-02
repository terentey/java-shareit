package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exception.IncorrectBookerId;
import ru.practicum.shareit.exception.IncorrectIdException;
import ru.practicum.shareit.item.dao.CommentRepository;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.CommentDtoRequest;
import ru.practicum.shareit.item.dto.CommentDtoResponse;
import ru.practicum.shareit.item.dto.ItemDtoRequest;
import ru.practicum.shareit.item.dto.ItemDtoResponse;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final static Sort SORT_BY_START_DESC = Sort.by(Sort.Direction.DESC, "start");
    private final static Sort SORT_BY_START_ASC = Sort.by(Sort.Direction.ASC, "start");
    private final static Sort SORT_BY_CREATED_DESC = Sort.by(Sort.Direction.DESC, "created");

    private final UserRepository userRepo;
    private final CommentRepository commentRepo;
    private final ItemRepository repository;
    private final BookingRepository bookingRepo;

    @Transactional
    @Override
    public ItemDtoResponse save(ItemDtoRequest itemDto, long userId) {
        return ItemMapper
                .mapToItemDto(repository
                        .saveAndFlush(ItemMapper
                                .mapToItem(itemDto, userRepo.findById(userId).orElseThrow(IncorrectIdException::new))));
    }

    @Transactional
    @Override
    public CommentDtoResponse saveComment(CommentDtoRequest commentDto, long itemId, long userId) {
        Item item = repository.findById(itemId).orElseThrow(IncorrectIdException::new);
        if (userId == item.getUser().getId()) throw new IncorrectIdException();
        Booking booking = bookingRepo.findByItemIdAndUserId(itemId, userId, SORT_BY_START_ASC)
                .stream().findFirst().orElseThrow(IncorrectBookerId::new);
        if (booking.getStatus() == Status.REJECTED || booking.getStart().isAfter(LocalDateTime.now())) {
            throw new IncorrectBookerId();
        }
        User user = booking.getUser();
        return CommentMapper.mapToCommentDto(commentRepo.saveAndFlush(CommentMapper.mapToComment(commentDto, item, user)));
    }

    @Transactional
    @Override
    public ItemDtoResponse update(ItemDtoRequest itemDto, long id, long userId) {
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
        return ItemMapper.mapToItemDto(item);
    }

    @Transactional
    @Override
    public ItemDtoResponse findById(long id, long userId) {
        Item item = repository.findById(id).orElseThrow(IncorrectIdException::new);
        List<Comment> comments = commentRepo.findByItemIn(List.of(item), SORT_BY_CREATED_DESC);
        List<Booking> bookings = bookingRepo.findAllByOwnerId(userId, SORT_BY_START_DESC)
                .stream()
                .filter(b -> !b.getStatus().equals(Status.REJECTED))
                .collect(toList());
        ItemDtoResponse itemDto;
        if (item.getUser().getId() == userId) {
            itemDto = ItemMapper.mapToItemDto(item, comments, bookings);
        } else {
            itemDto = ItemMapper.mapToItemDto(item, comments);
        }
        return itemDto;
    }

    @Transactional
    @Override
    public List<ItemDtoResponse> findAll(long userId) {
        return setCommentsAndBookings(repository.findItemsByUserId(userId));
    }

    @Transactional
    @Override
    public List<ItemDtoResponse> search(long userId, String text) {
        if (text.isBlank()) {
            return Collections.emptyList();
        }
        return setCommentsAndBookings(repository
                .findItemsByAvailableTrueAndDescriptionContainingIgnoreCaseOrNameContainingIgnoreCase(text, text));
    }

    private List<ItemDtoResponse> setCommentsAndBookings(List<Item> items) {
        Map<Item, List<Comment>> comments = commentRepo.findByItemIn(items, SORT_BY_CREATED_DESC)
                .stream()
                .collect(groupingBy(Comment::getItem, toList()));
        Map<Item, List<Booking>> bookings = bookingRepo.findByItemIn(items, SORT_BY_START_DESC)
                .stream()
                .filter(b -> !b.getStatus().equals(Status.REJECTED))
                .collect(groupingBy(Booking::getItem, toList()));
        return ItemMapper.mapToItemDto(items, comments, bookings);
    }
}
