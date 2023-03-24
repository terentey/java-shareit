package ru.practicum.shareit.util;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

public class BookingTest {
    public static Booking getNewBooking(Long id,
                                        LocalDateTime start,
                                        LocalDateTime end,
                                        Status status,
                                        Item item,
                                        User user) {
        Booking booking = getNewBooking(start, end, status, item, user);
        booking.setId(id);
        return booking;
    }

    public static Booking getNewBooking(LocalDateTime start,
                                        LocalDateTime end,
                                        Status status,
                                        Item item,
                                        User user) {
        Booking booking = new Booking();
        booking.setStart(start);
        booking.setEnd(end);
        booking.setStatus(status);
        booking.setItem(item);
        booking.setUser(user);
        return booking;
    }
}
