package ru.practicum.shareit.item.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemDtoResponse;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static ru.practicum.shareit.booking.model.Status.APPROVED;
import static ru.practicum.shareit.util.BookingTest.getNewBooking;
import static ru.practicum.shareit.util.CommentTest.getNewComment;
import static ru.practicum.shareit.util.ItemTest.getNewItem;
import static ru.practicum.shareit.util.UserTest.getNewUser;

class ItemMapperTest {
    private static final LocalDateTime NOW = LocalDateTime.now();
    private static final Instant NOW_INSTANT = Instant.now();

    private Item item;
    private Booking booking;
    private Comment comment;

    @BeforeEach
    void beforeEach() {
        User user = getNewUser("name", "email@mail.com");
        item = getNewItem("name", "description", true, user);
        User booker = getNewUser("booker", "booker@mail.com");
        booking = getNewBooking(NOW.minusDays(2), NOW.minusDays(1), APPROVED, item, booker);
        comment = getNewComment(1L, "text", NOW_INSTANT, item, booker);
    }

    @Test
    void mapToBookingDto_whenCommentsIsNotNull_thenReturnItemDtoWithComments() {
        List<Comment> comments = List.of(comment);

        ItemDtoResponse result = ItemMapper.mapToItemDto(item, comments);

        assertEquals(item.getId(), result.getId());
        assertEquals(item.getName(), result.getName());
        assertEquals(item.getDescription(), result.getDescription());
        assertEquals(item.getAvailable(), result.getAvailable());
        assertEquals(comment.getId(), result.getComments().get(0).getId());
        assertEquals(comment.getText(), result.getComments().get(0).getText());
        assertEquals(comment.getCreated(), result.getComments().get(0).getCreated());
        assertEquals(comment.getUser().getName(), result.getComments().get(0).getAuthorName());
    }

    @Test
    void mapToBookingDto_whenItemIsNull_thenThrowNPE() {
        List<Comment> comments = List.of(comment);

        assertThrows(NullPointerException.class, () -> ItemMapper.mapToItemDto(null, comments));
    }

    @Test
    void mapToBookingDto_whenCommentsIsNull_thenReturnItemDtoWithEmptyComments() {
        List<Comment> comments = null;

        ItemDtoResponse result = ItemMapper.mapToItemDto(item, comments);

        assertEquals(item.getId(), result.getId());
        assertEquals(item.getName(), result.getName());
        assertEquals(item.getDescription(), result.getDescription());
        assertEquals(item.getAvailable(), result.getAvailable());
        assertTrue(result.getComments().isEmpty());
    }

    @Test
    void mapToBookingDto_whenCommentsIsEmpty_thenReturnItemDtoWithEmptyComments() {
        List<Comment> comments = Collections.emptyList();

        ItemDtoResponse result = ItemMapper.mapToItemDto(item, comments);

        assertEquals(item.getId(), result.getId());
        assertEquals(item.getName(), result.getName());
        assertEquals(item.getDescription(), result.getDescription());
        assertEquals(item.getAvailable(), result.getAvailable());
        assertTrue(result.getComments().isEmpty());
    }

    @Test
    void mapToBookingDto_whenAllArgumentsIsCorrect_thenReturnItemDtoWithBookings() {
        List<Comment> comments = List.of(comment);
        List<Booking> bookings = List.of(booking);

        ItemDtoResponse result = ItemMapper.mapToItemDto(item, comments, bookings);

        assertEquals(item.getId(), result.getId());
        assertEquals(item.getName(), result.getName());
        assertEquals(item.getDescription(), result.getDescription());
        assertEquals(item.getAvailable(), result.getAvailable());
        assertEquals(comment.getId(), result.getComments().get(0).getId());
        assertEquals(comment.getText(), result.getComments().get(0).getText());
        assertEquals(comment.getCreated(), result.getComments().get(0).getCreated());
        assertEquals(comment.getUser().getName(), result.getComments().get(0).getAuthorName());
        assertEquals(booking.getId(), result.getLastBooking().getId());
        assertEquals(booking.getUser().getId(), result.getLastBooking().getBookerId());
        assertEquals(booking.getStart(), result.getLastBooking().getStart());
        assertEquals(booking.getEnd(), result.getLastBooking().getEnd());
        assertNull(result.getNextBooking());
    }

    @Test
    void mapToBookingDto_whenBookingsIsNull_thenReturnItemDtoWithoutBookings() {
        List<Comment> comments = List.of(comment);
        List<Booking> bookings = null;

        ItemDtoResponse result = ItemMapper.mapToItemDto(item, comments, bookings);

        assertEquals(item.getId(), result.getId());
        assertEquals(item.getName(), result.getName());
        assertEquals(item.getDescription(), result.getDescription());
        assertEquals(item.getAvailable(), result.getAvailable());
        assertEquals(comment.getId(), result.getComments().get(0).getId());
        assertEquals(comment.getText(), result.getComments().get(0).getText());
        assertEquals(comment.getCreated(), result.getComments().get(0).getCreated());
        assertEquals(comment.getUser().getName(), result.getComments().get(0).getAuthorName());
        assertNull(result.getLastBooking());
        assertNull(result.getNextBooking());
    }

    @Test
    void mapToBookingDto_whenBookingsIsEmpty_thenReturnItemDtoWithoutBookings() {
        List<Comment> comments = List.of(comment);
        List<Booking> bookings = Collections.emptyList();

        ItemDtoResponse result = ItemMapper.mapToItemDto(item, comments, bookings);

        assertEquals(item.getId(), result.getId());
        assertEquals(item.getName(), result.getName());
        assertEquals(item.getDescription(), result.getDescription());
        assertEquals(item.getAvailable(), result.getAvailable());
        assertEquals(comment.getId(), result.getComments().get(0).getId());
        assertEquals(comment.getText(), result.getComments().get(0).getText());
        assertEquals(comment.getCreated(), result.getComments().get(0).getCreated());
        assertEquals(comment.getUser().getName(), result.getComments().get(0).getAuthorName());
        assertNull(result.getLastBooking());
        assertNull(result.getNextBooking());
    }
}