package ru.practicum.shareit.request.mapper;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDtoRequest;
import ru.practicum.shareit.request.dto.ItemRequestDtoResponse;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ItemRequestMapper {
    public static ItemRequest mapToItemRequest(ItemRequestDtoRequest itemRequestDto, User user) {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription(itemRequestDto.getDescription());
        itemRequest.setUser(user);
        itemRequest.setCreated(LocalDateTime.now());
        return itemRequest;
    }

    public static ItemRequestDtoResponse mapToItemRequestDto(ItemRequest itemRequest) {
        return ItemRequestDtoResponse
                .builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreated())
                .build();
    }

    public static ItemRequestDtoResponse mapToItemRequestDto(ItemRequest itemRequest, List<Item> items) {
        ItemRequestDtoResponse itemRequestDto = mapToItemRequestDto(itemRequest);
        itemRequestDto.setItems(items.stream().map(ItemRequestMapper::mapToItemDto).collect(Collectors.toList()));
        return itemRequestDto;
    }

    public static List<ItemRequestDtoResponse> mapToItemRequestDto(List<ItemRequest> itemRequests,
                                                                   Map<ItemRequest, List<Item>> requests) {
        return itemRequests
                .stream()
                .map(i -> mapToItemRequestDto(i, requests.getOrDefault(i, Collections.emptyList())))
                .collect(Collectors.toList());
    }

    private static ItemRequestDtoResponse.ItemDto mapToItemDto(Item item) {
        return new ItemRequestDtoResponse.ItemDto(item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getItemRequest().getId());
    }
}
