package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.IncorrectIdException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.request.dto.ItemRequestDtoRequest;
import ru.practicum.shareit.request.dto.ItemRequestDtoResponse;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository repository;
    private final ItemRepository itemRepo;
    private final UserRepository userRepo;

    @Transactional
    @Override
    public ItemRequestDtoResponse save(ItemRequestDtoRequest itemRequestDto, long userId) {
        itemRequestDto.setUser(checkUser(userId));
        return ItemRequestMapper
                .mapToItemRequestDto(repository.saveAndFlush(ItemRequestMapper.mapToItemRequest(itemRequestDto)));
    }

    @Transactional(readOnly = true)
    @Override
    public ItemRequestDtoResponse findById(long id, long userId) {
        checkUser(userId);
        ItemRequest itemRequest = repository.findById(id).orElseThrow(IncorrectIdException::new);
        List<Item> items = itemRepo.findAllByItemRequestIn(Collections.singletonList(itemRequest));
        return ItemRequestMapper.mapToItemRequestDto(itemRequest, items);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ItemRequestDtoResponse> findAllByOwner(long userId) {
        List<ItemRequest> itemRequests = repository.findAllByUserOrderByCreated(checkUser(userId));
        return ItemRequestMapper.mapToItemRequestDto(itemRequests, findItems(itemRequests));
    }

    @Transactional(readOnly = true)
    @Override
    public List<ItemRequestDtoResponse> findAll(int from, Integer size, long userId) {
        checkUser(userId);
        if (size == null) {
            List<ItemRequest> itemRequests = repository.findAllByUserIdNotIn(userId);
            return ItemRequestMapper.mapToItemRequestDto(itemRequests, findItems(itemRequests));
        } else {
            int pageNum = from / size;
            List<ItemRequest> itemRequests = repository.findAllByUserIdNotIn(userId, PageRequest.of(pageNum, size));
            return ItemRequestMapper.mapToItemRequestDto(itemRequests, findItems(itemRequests));
        }
    }

    private Map<ItemRequest, List<Item>> findItems(List<ItemRequest> itemRequests) {
        return itemRepo
                .findAllByItemRequestIn(itemRequests)
                .stream()
                .collect(Collectors.groupingBy(Item::getItemRequest, Collectors.toList()));
    }

    private User checkUser(long userId) {
        return userRepo.findById(userId).orElseThrow(IncorrectIdException::new);
    }
}
