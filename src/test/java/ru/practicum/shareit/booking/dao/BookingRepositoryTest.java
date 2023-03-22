package ru.practicum.shareit.booking.dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static ru.practicum.shareit.booking.model.Status.APPROVED;
import static ru.practicum.shareit.booking.model.Status.WAITING;
import static ru.practicum.shareit.util.BookingTest.getNewBooking;
import static ru.practicum.shareit.util.ItemTest.getNewItem;
import static ru.practicum.shareit.util.UserTest.getNewUser;

@DataJpaTest
class BookingRepositoryTest {
    private static final Sort SORT_BY_START_DESC = Sort.by(Sort.Direction.DESC, "start");
    private static final LocalDateTime NOW = LocalDateTime.now();

    @Autowired
    private BookingRepository bookingRepo;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private ItemRepository itemRepo;

    private User user;
    private Item item;
    private User booker;

    @BeforeEach
    void beforeEach() {
        user = getNewUser("name", "email@mail.com");
        userRepo.save(user);

        item = getNewItem("name", "description", true, user);
        itemRepo.save(item);

        booker = getNewUser("booker", "booker@mail.com");
        userRepo.save(booker);
    }

    @Test
    void findByItemIdAndUserIdAndStatusApprovedAndStartBeforeNow() {
        Booking booking = getNewBooking(NOW.minusDays(2), NOW.minusDays(1), APPROVED, item, booker);
        bookingRepo.save(booking);

        List<Booking> bookings = bookingRepo.findByItemIdAndUserIdAndStatusApprovedAndStartBeforeNow(item.getId(),
                booker.getId(),
                SORT_BY_START_DESC);

        assertFalse(bookings.isEmpty());
    }

    @Test
    void findByItemInAndStatusNotIn() {
        Booking booking = getNewBooking(NOW.minusDays(2), NOW.minusDays(1), APPROVED, item, booker);
        bookingRepo.save(booking);

        List<Booking> bookings = bookingRepo.findByItemInAndStatusNotIn(List.of(item), WAITING, SORT_BY_START_DESC);

        assertFalse(bookings.isEmpty());
    }

    @Test
    void findAllByUserId() {
        Booking booking = getNewBooking(NOW.minusDays(2), NOW.minusDays(1), APPROVED, item, booker);
        bookingRepo.save(booking);

        List<Booking> bookings = bookingRepo.findAllByUserId(booker.getId(), SORT_BY_START_DESC);

        assertFalse(bookings.isEmpty());
    }

    @Test
    void findAllByUserIdAndStatus() {
        Booking booking = getNewBooking(NOW.minusDays(2), NOW.minusDays(1), APPROVED, item, booker);
        bookingRepo.save(booking);

        List<Booking> bookings = bookingRepo.findAllByUserIdAndStatus(booker.getId(), APPROVED, SORT_BY_START_DESC);

        assertFalse(bookings.isEmpty());
    }

    @Test
    void findAllByUserIdAndCurrentTime() {
        Booking booking = getNewBooking(NOW.minusDays(2), NOW.plusDays(1), APPROVED, item, booker);
        bookingRepo.save(booking);

        List<Booking> bookings = bookingRepo.findAllByUserIdAndCurrentTime(booker.getId(), SORT_BY_START_DESC);

        assertFalse(bookings.isEmpty());
    }

    @Test
    void findAllByUserIdAndStartAfter() {
        Booking booking = getNewBooking(NOW.plusDays(2), NOW.plusDays(3), APPROVED, item, booker);
        bookingRepo.save(booking);

        List<Booking> bookings = bookingRepo.findAllByUserIdAndStartAfter(booker.getId(), SORT_BY_START_DESC);

        assertFalse(bookings.isEmpty());
    }

    @Test
    void findAllByUserIdAndEndBefore() {
        Booking booking = getNewBooking(NOW.minusDays(2), NOW.minusDays(1), APPROVED, item, booker);
        bookingRepo.save(booking);

        List<Booking> bookings = bookingRepo.findAllByUserIdAndEndBefore(booker.getId(), SORT_BY_START_DESC);

        assertFalse(bookings.isEmpty());
    }

    @Test
    void findAllByOwnerIdAndStatusNotIn() {
        Booking booking = getNewBooking(NOW.minusDays(2), NOW.minusDays(1), APPROVED, item, booker);
        bookingRepo.save(booking);

        List<Booking> bookings = bookingRepo.findAllByOwnerIdAndStatusNotIn(user.getId(), WAITING, SORT_BY_START_DESC);

        assertFalse(bookings.isEmpty());
    }

    @Test
    void findAllByOwnerId() {
        Booking booking = getNewBooking(NOW.minusDays(2), NOW.minusDays(1), APPROVED, item, booker);
        bookingRepo.save(booking);

        List<Booking> bookings = bookingRepo.findAllByOwnerId(user.getId(), SORT_BY_START_DESC);

        assertFalse(bookings.isEmpty());
    }

    @Test
    void findAllByOwnerIdAndStatus() {
        Booking booking = getNewBooking(NOW.minusDays(2), NOW.minusDays(1), APPROVED, item, booker);
        bookingRepo.save(booking);

        List<Booking> bookings = bookingRepo.findAllByOwnerIdAndStatus(user.getId(), APPROVED, SORT_BY_START_DESC);

        assertFalse(bookings.isEmpty());
    }

    @Test
    void findAllByOwnerIdAndStatusAndCurrentTime() {
        Booking booking = getNewBooking(NOW.minusDays(2), NOW.plusDays(1), APPROVED, item, booker);
        bookingRepo.save(booking);

        List<Booking> bookings = bookingRepo.findAllByOwnerIdAndCurrentTime(user.getId(), SORT_BY_START_DESC);

        assertFalse(bookings.isEmpty());
    }

    @Test
    void findAllByOwnerIdAndStatusAndStartAfter() {
        Booking booking = getNewBooking(NOW.plusDays(2), NOW.plusDays(3), APPROVED, item, booker);
        bookingRepo.save(booking);

        List<Booking> bookings = bookingRepo.findAllByOwnerIdAndStartAfter(user.getId(), SORT_BY_START_DESC);

        assertFalse(bookings.isEmpty());
    }

    @Test
    void findAllByOwnerIdAndStatusAndEndBefore() {
        Booking booking = getNewBooking(NOW.minusDays(2), NOW.minusDays(1), APPROVED, item, booker);
        bookingRepo.save(booking);

        List<Booking> bookings = bookingRepo.findAllByOwnerIdAndEndBefore(user.getId(), SORT_BY_START_DESC);

        assertFalse(bookings.isEmpty());
    }

    @AfterEach
    void afterEach() {
        bookingRepo.deleteAll();
        userRepo.deleteAll();
        itemRepo.deleteAll();
    }
}