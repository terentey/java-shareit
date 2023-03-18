package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
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
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private static final Sort SORT_BY_START_DESC = Sort.by(Sort.Direction.DESC, "start");
    private static final Sort SORT_BY_START_ASC = Sort.by(Sort.Direction.ASC, "start");
    private static final Sort SORT_BY_CREATED_DESC = Sort.by(Sort.Direction.DESC, "created");

    private final UserRepository userRepo;
    private final CommentRepository commentRepo;
    private final ItemRepository repository;
    private final BookingRepository bookingRepo;
    private final ItemRequestRepository itemRequestRepo;

    @Transactional
    @Override
    public ItemDtoResponse save(ItemDtoRequest itemDto, long userId) {
        Item item = repository.saveAndFlush(ItemMapper
                .mapToItem(itemDto, userRepo.findById(userId)
                        .orElseThrow(IncorrectIdException::new)));
        if (itemDto.getRequestId() != null) {
            item.setItemRequest(itemRequestRepo
                    .findById(itemDto.getRequestId())
                    .orElseThrow(IncorrectIdException::new));
        }
        return ItemMapper
                .mapToItemDto(item);
    }

    @Transactional
    @Override
    public CommentDtoResponse saveComment(CommentDtoRequest commentDto, long itemId, long userId) {
        Item item = repository.findById(itemId).orElseThrow(IncorrectIdException::new);
        if (userId == item.getUser().getId()) {
            throw new IncorrectIdException();
        }
        Booking booking = bookingRepo
                .findByItemIdAndUserIdAndStatusApprovedAndStartBeforeNow(itemId, userId, SORT_BY_START_ASC)
                .stream().findFirst().orElseThrow(IncorrectBookerId::new);
        User user = booking.getUser();
        return CommentMapper.mapToCommentDto(commentRepo.saveAndFlush(CommentMapper.mapToComment(commentDto, item, user)));
    }

    @Transactional
    @Override
    public ItemDtoResponse update(ItemDtoRequest itemDto, long id, long userId) {
        Item item = repository.findById(id).orElseThrow(IncorrectIdException::new);
        if (userId != item.getUser().getId()) {
            throw new IncorrectIdException();
        }
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

    @Transactional(readOnly = true)
    @Override
    public ItemDtoResponse findById(long id, long userId) {
        Item item = repository.findById(id).orElseThrow(IncorrectIdException::new);
        List<Comment> comments = commentRepo.findByItemIn(List.of(item), SORT_BY_CREATED_DESC);
        List<Booking> bookings = bookingRepo.findAllByOwnerIdAndStatusNotIn(userId, Status.REJECTED, SORT_BY_START_DESC);
        ItemDtoResponse itemDto;
        if (item.getUser().getId() == userId) {
            itemDto = ItemMapper.mapToItemDto(item, comments, bookings);
        } else {
            itemDto = ItemMapper.mapToItemDto(item, comments);
        }
        return itemDto;
    }

    @Transactional(readOnly = true)
    @Override
    public List<ItemDtoResponse> findAll(long userId, int from, Integer size) {
        if (size == null) {
            return setCommentsAndBookings(repository.findItemsByUserId(userId));
        } else {
            int pageNum = from / size;
            return setCommentsAndBookings(repository.findItemsByUserId(userId, PageRequest.of(pageNum, from)));
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<ItemDtoResponse> search(long userId, String text, int from, Integer size) {
        if (text.isBlank()) {
            return Collections.emptyList();
        } else if (size == null) {
            return setCommentsAndBookings(repository
                    .findItemsByAvailableTrueAndDescriptionContainingIgnoreCaseOrNameContainingIgnoreCase(text, text));
        } else {
            int pageNum = from / size;
            return setCommentsAndBookings(repository.
                    findItemsByAvailableTrueAndDescriptionContainingIgnoreCaseOrNameContainingIgnoreCase(text,
                            text, PageRequest.of(pageNum, size)));
        }
    }

    private List<ItemDtoResponse> setCommentsAndBookings(List<Item> items) {
        Map<Item, List<Comment>> comments = commentRepo.findByItemIn(items, SORT_BY_CREATED_DESC)
                .stream()
                .collect(groupingBy(Comment::getItem, toList()));
        Map<Item, List<Booking>> bookings = bookingRepo
                .findByItemInAndStatusNotIn(items, Status.REJECTED, SORT_BY_START_DESC)
                .stream()
                .collect(groupingBy(Booking::getItem, toList()));
        return ItemMapper.mapToItemDto(items, comments, bookings);
    }
}
