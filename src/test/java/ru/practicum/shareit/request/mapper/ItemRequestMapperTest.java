package ru.practicum.shareit.request.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDtoResponse;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static ru.practicum.shareit.util.ItemTest.getNewItem;
import static ru.practicum.shareit.util.RequestTest.getNewRequest;
import static ru.practicum.shareit.util.UserTest.getNewUser;

class ItemRequestMapperTest {
    private static final LocalDateTime NOW = LocalDateTime.now();

    private ItemRequest request;
    private Item item;


    @BeforeEach
    void beforeEach() {
        User owner = getNewUser(1L, "owner", "owner@mail.com");
        User user = getNewUser(2L, "name", "email@mail.com");
        request = getNewRequest(1L, "description", NOW, user);
        item = getNewItem(1L, "name", "description", true, owner, request);
    }

    @Test
    void mapToItemRequestDto_whenItemsIsNotNull_thenReturnCorrectRequest() {
        List<Item> items = List.of(item);

        ItemRequestDtoResponse result = ItemRequestMapper.mapToItemRequestDto(request, items);

        assertEquals(request.getId(), result.getId());
        assertEquals(request.getDescription(), result.getDescription());
        assertEquals(request.getCreated(), result.getCreated());
        assertEquals(item.getName(), result.getItems().get(0).getName());
        assertEquals(item.getDescription(), result.getItems().get(0).getDescription());
        assertEquals(item.getAvailable(), result.getItems().get(0).getAvailable());
        assertEquals(item.getItemRequest().getId(), result.getItems().get(0).getRequestId());
    }

    @Test
    void mapToItemRequestDto_whenItemsIsEmpty_thenReturnCorrectRequestWithEmptyItems() {
        List<Item> items = Collections.emptyList();

        ItemRequestDtoResponse result = ItemRequestMapper.mapToItemRequestDto(request, items);

        assertEquals(request.getId(), result.getId());
        assertEquals(request.getDescription(), result.getDescription());
        assertEquals(request.getCreated(), result.getCreated());
        assertTrue(result.getItems().isEmpty());
    }

    @Test
    void mapToItemRequestDto_whenItemsIsNull_thenReturnCorrectRequestWithEmptyItems() {
        List<Item> items = null;

        ItemRequestDtoResponse result = ItemRequestMapper.mapToItemRequestDto(request, items);

        assertEquals(request.getId(), result.getId());
        assertEquals(request.getDescription(), result.getDescription());
        assertEquals(request.getCreated(), result.getCreated());
        assertTrue(result.getItems().isEmpty());
    }

    @Test
    void mapToItemRequestDto_whenItemRequestIsNull_thenReturnCorrectRequest() {
        List<Item> items = List.of(item);

        assertThrows(NullPointerException.class, () -> ItemRequestMapper.mapToItemRequestDto(null, items));
    }
}