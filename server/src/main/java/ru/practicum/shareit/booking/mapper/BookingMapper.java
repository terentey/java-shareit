package ru.practicum.shareit.booking.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BookingMapper {
    public static BookingDtoResponse mapToBookingDto(Booking booking) {
        return BookingDtoResponse
                .builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .status(booking.getStatus())
                .booker(new BookingDtoResponse.Booker(booking.getUser().getId(), booking.getUser().getName()))
                .item(new BookingDtoResponse.Item(booking.getItem().getId(), booking.getItem().getName()))
                .build();
    }

    public static Booking mapToBooking(BookingDtoRequest bookingDto, Status status, Item item, User booker) {
        Booking booking = new Booking();
        booking.setStart(bookingDto.getStart());
        booking.setEnd(bookingDto.getEnd());
        booking.setStatus(status);
        booking.setItem(item);
        booking.setUser(booker);
        return booking;
    }

    public static List<BookingDtoResponse> mapToBookingDto(List<Booking> bookings) {
        return bookings.stream().map(BookingMapper::mapToBookingDto).collect(Collectors.toList());
    }
}
