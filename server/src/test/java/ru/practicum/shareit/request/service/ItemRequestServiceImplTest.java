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
import ru.practicum.shareit.request.dto.ItemRequestDtoRequest;
import ru.practicum.shareit.request.dto.ItemRequestDtoResponse;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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
    void save() {
        ItemRequestDtoRequest dto = new ItemRequestDtoRequest();
        dto.setDescription("description");
        when(userRepo.findById(anyLong())).thenReturn(Optional.ofNullable(user));
        when(requestRepo.saveAndFlush(any())).thenReturn(request);

        ItemRequestDtoResponse result = service.save(dto, 2L);

        assertEquals(request.getId(), result.getId());
        assertEquals(request.getDescription(), result.getDescription());
        assertEquals(request.getCreated(), result.getCreated());
        assertEquals(request.getUser(), user);
    }

    @Test
    void findById() {
        when(userRepo.findById(anyLong())).thenReturn(Optional.ofNullable(user));
        when(requestRepo.findById(anyLong())).thenReturn(Optional.ofNullable(request));
        when(itemRepo.findAllByItemRequestIn(any())).thenReturn(List.of(item));

        ItemRequestDtoResponse result = service.findById(1L, 2L);

        assertEquals(request.getId(), result.getId());
        assertEquals(request.getDescription(), result.getDescription());
        assertEquals(request.getCreated(), result.getCreated());
        assertEquals(item.getId(), result.getItems().get(0).getId());
    }

    @Test
    void findAllByOwner() {
        when(userRepo.findById(anyLong())).thenReturn(Optional.ofNullable(user));
        when(requestRepo.findAllByUserOrderByCreated(any())).thenReturn(List.of(request));
        when(itemRepo.findAllByItemRequestIn(any())).thenReturn(List.of(item));

        List<ItemRequestDtoResponse> result = service.findAllByOwner(1L);

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