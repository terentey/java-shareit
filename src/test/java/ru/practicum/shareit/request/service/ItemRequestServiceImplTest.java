package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.request.dto.ItemRequestDtoResponse;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static ru.practicum.shareit.util.ItemTest.getNewItem;
import static ru.practicum.shareit.util.RequestTest.getNewRequest;
import static ru.practicum.shareit.util.UserTest.getNewUser;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceImplTest {
    private static final LocalDateTime NOW = LocalDateTime.now();

    @InjectMocks
    private ItemRequestServiceImpl service;

    @Mock
    private ItemRequestRepository requestRepo;
    @Mock
    private ItemRepository itemRepo;
    @Mock
    private UserRepository userRepo;

    private User user;
    private Item item;
    private ItemRequest request;

    @BeforeEach
    void beforeEach() {
        User owner = getNewUser(1L, "owner", "owner@mail.com");
        user = getNewUser(2L, "name", "email@mail.com");
        request = getNewRequest(1L, "description", NOW, user);
        item = getNewItem(1L, "name", "description", true, owner, request);
    }

    @Test
    void findAll_whenSizeIsNull_thenReturnRequests() {
        when(userRepo.findById(2L)).thenReturn(Optional.ofNullable(user));
        List<ItemRequest> requests = List.of(request);
        when(requestRepo.findAllByUserIdNotIn(2L)).thenReturn(requests);
        when(itemRepo.findAllByItemRequestIn(requests)).thenReturn(List.of(item));

        List<ItemRequestDtoResponse> result = service.findAll(0, null, 2L);

        assertEquals(request.getId(), result.get(0).getId());
        assertEquals(request.getDescription(), result.get(0).getDescription());
        assertEquals(request.getCreated(), result.get(0).getCreated());
        assertEquals(item.getId(), result.get(0).getItems().get(0).getId());
    }

    @Test
    void findAll_whenSizeIsNotNull_thenReturnRequests() {
        when(userRepo.findById(2L)).thenReturn(Optional.ofNullable(user));
        List<ItemRequest> requests = List.of(request);
        when(requestRepo.findAllByUserIdNotIn(2L, PageRequest.of(0 / 1, 1))).thenReturn(requests);
        when(itemRepo.findAllByItemRequestIn(requests)).thenReturn(List.of(item));

        List<ItemRequestDtoResponse> result = service.findAll(0, 1, 2L);

        assertEquals(request.getId(), result.get(0).getId());
        assertEquals(request.getDescription(), result.get(0).getDescription());
        assertEquals(request.getCreated(), result.get(0).getCreated());
        assertEquals(item.getId(), result.get(0).getItems().get(0).getId());
    }
}