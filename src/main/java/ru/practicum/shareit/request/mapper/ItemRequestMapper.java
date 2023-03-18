package ru.practicum.shareit.request.mapper;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDtoRequest;
import ru.practicum.shareit.request.dto.ItemRequestDtoResponse;
import ru.practicum.shareit.request.model.ItemRequest;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ItemRequestMapper {
    public static ItemRequest mapToItemRequest(ItemRequestDtoRequest itemRequestDto) {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription(itemRequestDto.getDescription());
        itemRequest.setUser(itemRequestDto.getUser());
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
        if (items != null) {
            itemRequestDto.setItems(items.stream().map(ItemRequestMapper::mapToItemDto).collect(Collectors.toList()));
        } else {
            itemRequestDto.setItems(Collections.emptyList());
        }
        return itemRequestDto;
    }

    public static List<ItemRequestDtoResponse> mapToItemRequestDto(List<ItemRequest> itemRequests,
                                                                   Map<ItemRequest, List<Item>> requests) {
        return itemRequests
                .stream()
                .map(i -> mapToItemRequestDto(i, requests.get(i)))
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
