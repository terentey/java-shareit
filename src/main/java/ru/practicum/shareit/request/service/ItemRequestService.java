package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDtoRequest;
import ru.practicum.shareit.request.dto.ItemRequestDtoResponse;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDtoResponse save(ItemRequestDtoRequest itemRequestDto, long userId);

    ItemRequestDtoResponse findById(long id, long userId);

    List<ItemRequestDtoResponse> findAllByOwner(long userId);

    List<ItemRequestDtoResponse> findAll(int from, Integer size, long userId);
}
