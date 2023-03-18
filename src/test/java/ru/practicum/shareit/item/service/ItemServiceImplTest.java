package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.IncorrectBookerId;
import ru.practicum.shareit.exception.IncorrectIdException;
import ru.practicum.shareit.item.dao.CommentRepository;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.CommentDtoRequest;
import ru.practicum.shareit.item.dto.CommentDtoResponse;
import ru.practicum.shareit.item.dto.ItemDtoRequest;
import ru.practicum.shareit.item.dto.ItemDtoResponse;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static ru.practicum.shareit.booking.model.Status.APPROVED;
import static ru.practicum.shareit.booking.model.Status.REJECTED;
import static ru.practicum.shareit.util.BookingTest.getNewBooking;
import static ru.practicum.shareit.util.CommentTest.getNewComment;
import static ru.practicum.shareit.util.ItemTest.getNewItem;
import static ru.practicum.shareit.util.RequestTest.getNewRequest;
import static ru.practicum.shareit.util.UserTest.getNewUser;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {
    private static final Sort SORT_BY_START_DESC = Sort.by(Sort.Direction.DESC, "start");
    private static final Sort SORT_BY_START_ASC = Sort.by(Sort.Direction.ASC, "start");
    private static final Sort SORT_BY_CREATED_DESC = Sort.by(Sort.Direction.DESC, "created");
    private static final LocalDateTime NOW = LocalDateTime.now();
    private static final Instant NOW_INSTANT = Instant.now();

    @InjectMocks
    private ItemServiceImpl service;

    @Mock
    private UserRepository userRepo;
    @Mock
    private CommentRepository commentRepo;
    @Mock
    private ItemRepository itemRepo;
    @Mock
    private BookingRepository bookingRepo;
    @Mock
    private ItemRequestRepository itemRequestRepo;

    private User owner;
    private Item item;
    private Booking booking;
    private Comment comment;
    private ItemRequest request;
    private ItemDtoRequest itemDtoRequest;
    private ItemDtoResponse itemDtoResponse;
    private CommentDtoRequest commentDtoRequest;
    private CommentDtoResponse commentDtoResponse;

    @BeforeEach
    void beforeEach() {
        owner = getNewUser(1L, "name", "email@mail.com");
        item = getNewItem("name", "description", true, owner);
        User booker = getNewUser("booker", "booker@mail.com");
        booking = getNewBooking(1L, NOW.minusDays(2), NOW.minusDays(1), APPROVED, item, booker);
        comment = getNewComment(1L, "text", NOW_INSTANT, item, booker);
        User requester = getNewUser("requester", "requester@maol.com");
        request = getNewRequest(1L, "description", NOW, requester);
        itemDtoRequest = ItemDtoRequest
                .builder()
                .name("name")
                .description("description")
                .available(true)
                .build();
        itemDtoResponse = ItemDtoResponse
                .builder()
                .name("name")
                .description("description")
                .available(true)
                .build();
        commentDtoRequest = CommentDtoRequest
                .builder()
                .id(1L)
                .text("text")
                .build();
        commentDtoResponse = CommentDtoResponse
                .builder()
                .id(1L)
                .text("text")
                .created(NOW_INSTANT)
                .authorName(booker.getName())
                .build();
    }

    @Test
    void save_whenAllArgumentsIsCorrectAndRequestIdIsNull_thenReturnItemDtoWithoutItemRequest() {
        when(userRepo.findById(1L)).thenReturn(Optional.ofNullable(owner));
        when(itemRepo.saveAndFlush(any(Item.class))).thenReturn(item);

        ItemDtoResponse result = service.save(itemDtoRequest, 1L);

        assertEquals(itemDtoResponse, result);
    }

    @Test
    void save_whenAllArgumentsIsCorrectAndRequestIdIsNotNull_thenReturnItemDtoWithItemRequest() {
        when(userRepo.findById(1L)).thenReturn(Optional.ofNullable(owner));
        when(itemRepo.saveAndFlush(any(Item.class))).thenReturn(item);
        itemDtoRequest.setRequestId(1L);
        when(itemRequestRepo.findById(1L)).thenReturn(Optional.ofNullable(request));
        itemDtoResponse.setRequestId(1L);

        ItemDtoResponse result = service.save(itemDtoRequest, 1L);

        assertEquals(itemDtoResponse, result);
    }

    @Test
    void save_whenItemDtoRequestIsNull_thenThrowNPE() {
        when(userRepo.findById(1L)).thenReturn(Optional.ofNullable(owner));

        assertThrows(NullPointerException.class, () -> service.save(itemDtoRequest, 1L));
    }

    @Test
    void saveComment_whenAllArgumentsIsCorrect_thenReturnCorrectCommentDtoResponse() {
        when(itemRepo.findById(1L)).thenReturn(Optional.ofNullable(item));
        owner.setId(1L);
        when(bookingRepo.findByItemIdAndUserIdAndStatusApprovedAndStartBeforeNow(1L, 2L, SORT_BY_START_ASC))
                .thenReturn(List.of(booking));
        when(commentRepo.saveAndFlush(any(Comment.class))).thenReturn(comment);

        CommentDtoResponse result = service.saveComment(commentDtoRequest, 1L, 2L);

        assertEquals(commentDtoResponse, result);
    }

    @Test
    void saveComment_whenUserIdIsOwnerId_thenThrowIncorrectIdException() {
        when(itemRepo.findById(1L)).thenReturn(Optional.ofNullable(item));

        assertThrows(IncorrectIdException.class, () -> service.saveComment(commentDtoRequest, 1L, 1L));
    }

    @Test
    void saveComment_whenBookingIsNotExist_thenThrowIncorrectBookingId() {
        when(itemRepo.findById(1L)).thenReturn(Optional.ofNullable(item));
        when(bookingRepo
                .findByItemIdAndUserIdAndStatusApprovedAndStartBeforeNow(1L, 2L, SORT_BY_START_ASC))
                .thenReturn(Collections.emptyList());

        assertThrows(IncorrectBookerId.class, () -> service.saveComment(commentDtoRequest, 1L, 2L));

        verify(commentRepo, never()).saveAndFlush(any());
    }

    @Test
    void saveComment_whenCommentDtoRequestIsNull_thenThrowNPE() {
        when(itemRepo.findById(1L)).thenReturn(Optional.ofNullable(item));
        when(bookingRepo.findByItemIdAndUserIdAndStatusApprovedAndStartBeforeNow(1L, 2L, SORT_BY_START_ASC))
                .thenReturn(List.of(booking));

        assertThrows(NullPointerException.class, () -> service.saveComment(null, 1L, 2L));
    }

    @Test
    void update_whenAllArgumentsIsNotNull_thenReturnItemDtoWithUpdatedNameAndUpdatedDescriptionAndUpdatedAvailable() {
        when(itemRepo.findById(1L)).thenReturn(Optional.ofNullable(item));
        ItemDtoRequest update = ItemDtoRequest
                .builder()
                .name("update")
                .description("update")
                .available(false)
                .build();
        ItemDtoResponse correctResult = ItemDtoResponse
                .builder()
                .name("update")
                .description("update")
                .available(false)
                .build();

        ItemDtoResponse result = service.update(update, 1L, 1L);

        assertEquals(correctResult, result);
    }

    @Test
    void update_whenItemDtoRequestWithName_thenReturnItemDtoWithUpdatedName() {
        when(itemRepo.findById(1L)).thenReturn(Optional.ofNullable(item));
        ItemDtoRequest update = ItemDtoRequest
                .builder()
                .name("update")
                .build();
        ItemDtoResponse correctResult = ItemDtoResponse
                .builder()
                .name("update")
                .description("description")
                .available(true)
                .build();

        ItemDtoResponse result = service.update(update, 1L, 1L);

        assertEquals(correctResult, result);
    }

    @Test
    void update_whenItemDtoRequestWithDescription_thenReturnItemDtoWithUpdatedDescription() {
        when(itemRepo.findById(1L)).thenReturn(Optional.ofNullable(item));
        ItemDtoRequest update = ItemDtoRequest
                .builder()
                .description("update")
                .build();
        ItemDtoResponse correctResult = ItemDtoResponse
                .builder()
                .name("name")
                .description("update")
                .available(true)
                .build();

        ItemDtoResponse result = service.update(update, 1L, 1L);

        assertEquals(correctResult, result);
    }

    @Test
    void update_whenItemDtoRequestWithAvailable_thenReturnItemDtoWithUpdatedAvailable() {
        when(itemRepo.findById(1L)).thenReturn(Optional.ofNullable(item));
        ItemDtoRequest update = ItemDtoRequest
                .builder()
                .available(false)
                .build();
        ItemDtoResponse correctResult = ItemDtoResponse
                .builder()
                .name("name")
                .description("description")
                .available(false)
                .build();

        ItemDtoResponse result = service.update(update, 1L, 1L);

        assertEquals(correctResult, result);
    }

    @Test
    void update_whenUserIsNotOwner_thenThrowIncorrectIdException() {
        when(itemRepo.findById(1L)).thenReturn(Optional.ofNullable(item));

        assertThrows(IncorrectIdException.class, () -> service.update(itemDtoRequest, 1L, 2L));
    }

    @Test
    void update_whenItemDtoRequestIsNull_thenThrowNPE() {
        when(itemRepo.findById(1L)).thenReturn(Optional.ofNullable(item));

        assertThrows(NullPointerException.class, () -> service.update(null, 1L, 1L));
    }

    @Test
    void findById_whenCommentsIsNotNullAndBookingsIsNotNullAndUserIsOwner_thenReturnItemDtoWithCommentsAndBookings() {
        when(itemRepo.findById(1L)).thenReturn(Optional.ofNullable(item));
        when(commentRepo.findByItemIn(List.of(item), SORT_BY_CREATED_DESC)).thenReturn(List.of(comment));
        when(bookingRepo.findAllByOwnerIdAndStatusNotIn(1L, REJECTED, SORT_BY_START_DESC))
                .thenReturn(List.of(booking));
        ItemDtoResponse correctResult = ItemMapper.mapToItemDto(item, List.of(comment), List.of(booking));

        ItemDtoResponse result = service.findById(1L, 1L);

        assertEquals(correctResult, result);
    }

    @Test
    void findById_whenCommentsIsNotNullAndBookingsNotIsNullAndUserIsNotOwner_thenReturnItemDtoWithCommentsAndBookings() {
        when(itemRepo.findById(1L)).thenReturn(Optional.ofNullable(item));
        when(commentRepo.findByItemIn(List.of(item), SORT_BY_CREATED_DESC)).thenReturn(List.of(comment));
        when(bookingRepo.findAllByOwnerIdAndStatusNotIn(2L, REJECTED, SORT_BY_START_DESC))
                .thenReturn(List.of(booking));
        ItemDtoResponse correctResult = ItemMapper.mapToItemDto(item, List.of(comment));

        ItemDtoResponse result = service.findById(1L, 2L);

        assertEquals(correctResult, result);
    }

    @Test
    void findById_whenCommentsIsNullAndBookingsIsNullAndUserIsOwner_thenReturnItemDtoWithCommentsIsEmpty() {
        when(itemRepo.findById(1L)).thenReturn(Optional.ofNullable(item));
        ItemDtoResponse correctResult = ItemMapper.mapToItemDto(item, emptyList());

        ItemDtoResponse result = service.findById(1L, 1L);

        assertEquals(correctResult, result);
    }

    @Test
    void findById_whenCommentsIsNullAndBookingsIsNullAndUserIsNotOwner_thenReturnItemDtoWithCommentsIsEmpty() {
        when(itemRepo.findById(1L)).thenReturn(Optional.ofNullable(item));
        ItemDtoResponse correctResult = ItemMapper.mapToItemDto(item, emptyList());

        ItemDtoResponse result = service.findById(1L, 2L);

        assertEquals(correctResult, result);
    }

    @Test
    void findById_whenCommentsIsNotNullAndBookingsIsNullAndUserIsOwner_thenReturnItemDtoWithCommentsIsNotEmpty() {
        when(itemRepo.findById(1L)).thenReturn(Optional.ofNullable(item));
        when(commentRepo.findByItemIn(List.of(item), SORT_BY_CREATED_DESC)).thenReturn(List.of(comment));
        ItemDtoResponse correctResult = ItemMapper.mapToItemDto(item, List.of(comment));

        ItemDtoResponse result = service.findById(1L, 1L);

        assertEquals(correctResult, result);
    }

    @Test
    void findById_whenCommentsIsNotNullAndBookingsIsNullAndUserIsNotOwner_thenReturnItemDtoWithCommentsIsNotEmpty() {
        when(itemRepo.findById(1L)).thenReturn(Optional.ofNullable(item));
        when(commentRepo.findByItemIn(List.of(item), SORT_BY_CREATED_DESC)).thenReturn(List.of(comment));
        ItemDtoResponse correctResult = ItemMapper.mapToItemDto(item, List.of(comment));

        ItemDtoResponse result = service.findById(1L, 2L);

        assertEquals(correctResult, result);
    }

    @Test
    void findAll_whenSizeIsNull_thenReturnItems() {
        when(itemRepo.findItemsByUserId(1L)).thenReturn(List.of(item));
        itemDtoResponse.setComments(emptyList());

        List<ItemDtoResponse> result = service.findAll(1L, 0, null);

        assertEquals(List.of(itemDtoResponse), result);
    }

    @Test
    void findAll_whenSizeIsNotNull_thenReturnItems() {
        when(itemRepo.findItemsByUserId(1L, PageRequest.of(2, 2))).thenReturn(List.of(item));
        itemDtoResponse.setComments(emptyList());

        List<ItemDtoResponse> result = service.findAll(1L, 2, 1);

        assertEquals(List.of(itemDtoResponse), result);
    }

    @Test
    void search_whenTextIsBlank_thenReturnEmptyList() {
        String text = "    ";

        List<ItemDtoResponse> result = service.search(1L, text, 2, 1);

        assertEquals(emptyList(), result);
    }

    @Test
    void search_whenTextIsNotBlankAndSizeIsNull_thenReturnItems() {
        String text = "name";
        when(itemRepo.findItemsByAvailableTrueAndDescriptionContainingIgnoreCaseOrNameContainingIgnoreCase(text, text))
                .thenReturn(List.of(item));
        itemDtoResponse.setComments(emptyList());

        List<ItemDtoResponse> result = service.search(1L, text, 0, null);

        assertEquals(List.of(itemDtoResponse), result);
    }

    @Test
    void search_whenTextIsNotBlankAndSizeIsNotNull_thenReturnItems() {
        String text = "name";
        when(itemRepo.findItemsByAvailableTrueAndDescriptionContainingIgnoreCaseOrNameContainingIgnoreCase(text,
                text, PageRequest.of(2, 1)))
                .thenReturn(List.of(item));
        itemDtoResponse.setComments(emptyList());

        List<ItemDtoResponse> result = service.search(1L, text, 2, 1);

        assertEquals(List.of(itemDtoResponse), result);
    }

    @AfterEach
    void afterEach() {
        userRepo.deleteAll();
        commentRepo.deleteAll();
        itemRepo.deleteAll();
        bookingRepo.deleteAll();
        itemRequestRepo.deleteAll();
    }
}