package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;

import java.util.List;

public interface BookingService {
    BookingDtoResponse save(BookingDtoRequest bookingDto, long userId);

    BookingDtoResponse update(long id, long userId, boolean approved);

    BookingDtoResponse findById(long id, long userId);

    List<BookingDtoResponse> findAllByUserId(long userId, String status, int from, int size);

    List<BookingDtoResponse> findAllByOwnerId(long ownerId, String status, int from, int size);
}
