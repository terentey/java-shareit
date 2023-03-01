package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

public interface BookingService {
    BookingDto save(BookingDto bookingDto, long userId);

    BookingDto update(long id, long userId, boolean approved);

    BookingDto findById(long id, long userId);

    List<BookingDto> findAllByUserId(long userId, String status);

    List<BookingDto> findAllByOwnerId(long ownerId, String status);
}
