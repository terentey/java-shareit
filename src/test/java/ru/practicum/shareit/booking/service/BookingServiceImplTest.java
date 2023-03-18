package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.IncorrectIdException;
import ru.practicum.shareit.exception.IncorrectState;
import ru.practicum.shareit.exception.UnavailableItemException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static ru.practicum.shareit.booking.model.Status.*;
import static ru.practicum.shareit.util.BookingTest.getNewBooking;
import static ru.practicum.shareit.util.ItemTest.getNewItem;
import static ru.practicum.shareit.util.UserTest.getNewUser;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {
    private static final Sort SORT_BY_START_DESC = Sort.by(Sort.Direction.DESC, "start");
    private static final LocalDateTime NOW = LocalDateTime.now();

    @InjectMocks
    private BookingServiceImpl service;

    @Mock
    private BookingRepository bookingRepo;
    @Mock
    private ItemRepository itemRepo;
    @Mock
    private UserRepository userRepo;

    private User owner;
    private Item item;
    private User booker;
    private Booking booking;
    private BookingDtoRequest bookingDtoRequest;
    private BookingDtoResponse bookingDtoResponse;

    @BeforeEach
    void beforeEach() {
        LocalDateTime start = NOW.minusDays(2);
        LocalDateTime end = NOW.minusDays(1);
        owner = getNewUser(1L, "name", "email@mail.com");
        item = getNewItem(1L, "name", "description", true, owner);
        booker = getNewUser(2L, "booker", "booker@mail.com");
        booking = getNewBooking(1L, start, end, WAITING, item, booker);
        bookingDtoRequest = BookingDtoRequest
                .builder()
                .start(start)
                .end(end)
                .itemId(1L)
                .build();
        bookingDtoResponse = BookingDtoResponse
                .builder()
                .id(1L)
                .start(start)
                .end(end)
                .status(WAITING)
                .booker(new BookingDtoResponse.Booker(2L, "booker"))
                .item(new BookingDtoResponse.Item(1L, "name"))
                .build();
    }

    @Test
    void save_whenUserIdIsCorrectAndItemIsAvailable_thenReturnBookingDto() {
        when(itemRepo.findById(1L)).thenReturn(Optional.ofNullable(item));
        when(userRepo.findById(2L)).thenReturn(Optional.ofNullable(booker));
        when(bookingRepo.saveAndFlush(any(Booking.class))).thenReturn(booking);

        BookingDtoResponse result = service.save(bookingDtoRequest, 2L);

        assertEquals(bookingDtoResponse, result);
    }

    @Test
    void save_whenUserIdIsOwnerId_thenThrowIncorrectIdException() {
        when(itemRepo.findById(1L)).thenReturn(Optional.ofNullable(item));
        when(userRepo.findById(1L)).thenReturn(Optional.ofNullable(owner));

        assertThrows(IncorrectIdException.class, () -> service.save(bookingDtoRequest, 1L));
    }

    @Test
    void save_whenItemIsNotAvailable_thenThrowUnavailableItemException() {
        when(itemRepo.findById(1L)).thenReturn(Optional.ofNullable(item));
        item.setAvailable(false);
        when(userRepo.findById(2L)).thenReturn(Optional.ofNullable(booker));

        assertThrows(UnavailableItemException.class, () -> service.save(bookingDtoRequest, 2L));
    }

    @Test
    void update_whenBookingIsApproved_thenReturnApprovedBookingDto() {
        when(bookingRepo.findById(1L)).thenReturn(Optional.ofNullable(booking));
        bookingDtoResponse.setStatus(APPROVED);

        BookingDtoResponse result = service.update(1L, 1L, true);

        assertEquals(bookingDtoResponse, result);
    }

    @Test
    void update_whenBookingIsNotApproved_thenReturnREjectedBookingDto() {
        when(bookingRepo.findById(1L)).thenReturn(Optional.ofNullable(booking));
        bookingDtoResponse.setStatus(REJECTED);

        BookingDtoResponse result = service.update(1L, 1L, false);

        assertEquals(bookingDtoResponse, result);
    }

    @Test
    void update_whenCurrentIdIsNotOwnerId_thenThrowIncorrectException() {
        when(bookingRepo.findById(1L)).thenReturn(Optional.ofNullable(booking));

        assertThrows(IncorrectIdException.class, () -> service.update(1L, 2L, true));
    }

    @Test
    void update_whenBookingIsApproved_thenThrowUnavailableItemException() {
        when(bookingRepo.findById(1L)).thenReturn(Optional.ofNullable(booking));
        booking.setStatus(APPROVED);

        assertThrows(UnavailableItemException.class, () -> service.update(1L, 1L, true));
    }

    @Test
    void findAllByUserId_whenStatusIsNullAndSizeIsNull_thenThrowIncorrectState() {
        assertThrows(IncorrectState.class, () -> service.findAllByUserId(1L, null, 1, null));
    }

    @Test
    void findAllByUserId_whenStatusIsNullAndSizeIsNotNull_thenThrowIncorrectState() {
        assertThrows(IncorrectState.class, () -> service.findAllByUserId(1L, null, 1, 1));
    }

    @Test
    void findAllByUserId_whenStatusIsALLAndSizeIsNull_thenReturnBookings() {
        when(userRepo.findById(2L)).thenReturn(Optional.ofNullable(booker));
        when(bookingRepo.findAllByUserId(2L, SORT_BY_START_DESC)).thenReturn(List.of(booking));

        List<BookingDtoResponse> result = service.findAllByUserId(2L, "ALL", 0, null);

        assertEquals(List.of(bookingDtoResponse), result);
    }

    @Test
    void findAllByUserId_whenStatusIsAllAndSizeIsNull_thenReturnBookings() {
        assertThrows(IncorrectState.class, () -> service.findAllByUserId(2L, "All", 0, null));
    }

    @Test
    void findAllByUserId_whenStatusIsBlankAndSizeIsNull_thenReturnBookings() {
        assertThrows(IncorrectState.class, () -> service.findAllByUserId(2L, "    ", 0, null));
    }

    @Test
    void findAllByUserId_whenStatusIsALLAndSizeIsNotNull_thenReturnBookings() {
        when(userRepo.findById(2L)).thenReturn(Optional.ofNullable(booker));
        when(bookingRepo.findAllByUserId(2L, PageRequest.of(2, 1, SORT_BY_START_DESC)))
                .thenReturn(new PageImpl<>(List.of(booking)));

        List<BookingDtoResponse> result = service.findAllByUserId(2L, "ALL", 2, 1);

        assertEquals(List.of(bookingDtoResponse), result);
    }

    @Test
    void findAllByUserId_whenStatusIsAllAndSizeIsNotNull_thenReturnBookings() {
        assertThrows(IncorrectState.class, () -> service.findAllByUserId(2L, "All", 2, 1));
    }

    @Test
    void findAllByUserId_whenStatusIsBlankAndSizeIsNotNull_thenReturnBookings() {
        assertThrows(IncorrectState.class, () -> service.findAllByUserId(2L, "    ", 2, 1));
    }

    @Test
    void findAllByOwnerId_whenStatusIsNullAndSizeIsNull_thenThrowIncorrectState() {
        assertThrows(IncorrectState.class, () -> service.findAllByOwnerId(2L, null, 1, null));
    }

    @Test
    void findAllByOwnerId_whenStatusIsNullAndSizeIsNotNull_thenThrowIncorrectState() {
        assertThrows(IncorrectState.class, () -> service.findAllByOwnerId(2L, null, 1, 1));
    }

    @Test
    void findAllByOwnerId_whenStatusIsALLAndSizeIsNull_thenReturnBookings() {
        when(userRepo.findById(1L)).thenReturn(Optional.ofNullable(booker));
        when(bookingRepo.findAllByOwnerId(1L, SORT_BY_START_DESC)).thenReturn(List.of(booking));

        List<BookingDtoResponse> result = service.findAllByOwnerId(1L, "ALL", 0, null);

        assertEquals(List.of(bookingDtoResponse), result);
    }

    @Test
    void findAllByOwnerId_whenStatusIsAllAndSizeIsNull_thenReturnBookings() {
        assertThrows(IncorrectState.class, () -> service.findAllByOwnerId(1L, "All", 0, null));
    }

    @Test
    void findAllByOwnerId_whenStatusIsBlankAndSizeIsNull_thenReturnBookings() {
        assertThrows(IncorrectState.class, () -> service.findAllByOwnerId(1L, "    ", 0, null));
    }

    @Test
    void findAllByOwnerId_whenStatusIsALLAndSizeIsNotNull_thenReturnBookings() {
        when(userRepo.findById(1L)).thenReturn(Optional.ofNullable(owner));
        when(bookingRepo.findAllByOwnerId(1L, PageRequest.of(2, 1, SORT_BY_START_DESC)))
                .thenReturn(new PageImpl<>(List.of(booking)));

        List<BookingDtoResponse> result = service.findAllByOwnerId(1L, "ALL", 2, 1);

        assertEquals(List.of(bookingDtoResponse), result);
    }

    @Test
    void findAllByOwnerId_whenStatusIsAllAndSizeIsNotNull_thenReturnBookings() {
        assertThrows(IncorrectState.class, () -> service.findAllByOwnerId(1L, "All", 2, 1));
    }

    @Test
    void findAllByOwnerId_whenStatusIsBlankAndSizeIsNotNull_thenReturnBookings() {
        assertThrows(IncorrectState.class, () -> service.findAllByOwnerId(1L, "    ", 2, 1));
    }

    @AfterEach
    void afterEach() {
        bookingRepo.deleteAll();
        itemRepo.deleteAll();
        userRepo.deleteAll();
    }
}