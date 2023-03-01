package ru.practicum.shareit.booking.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BookingMapper {
    public static BookingDto mapToBookingDto(Booking booking) {
        return BookingDto
                .builder()
                .id(booking.getId())
                .bookerId(booking.getUser().getId())
                .start(booking.getStart().toString())
                .end(booking.getEnd().toString())
                .status(booking.getStatus())
                .booker(UserMapper.mapToUserDto(booking.getUser()))
                .item(ItemMapper.mapToItemDto(booking.getItem()))
                .build();
    }

    public static BookingDto mapToBookingDtoForItemDto(Booking booking) {
        return BookingDto
                .builder()
                .id(booking.getId())
                .bookerId(booking.getUser().getId())
                .build();
    }

    public static Booking mapToBooking(BookingDto bookingDto, Status status, Item item, User booker) {
        Booking booking = new Booking();
        booking.setStart(LocalDateTime.parse(bookingDto.getStart()));
        booking.setEnd(LocalDateTime.parse(bookingDto.getEnd()));
        booking.setStatus(status);
        booking.setItem(item);
        booking.setUser(booker);
        return booking;
    }

    public static List<BookingDto> mapToBookingDto(List<Booking> bookings) {
        return bookings.stream().map(BookingMapper::mapToBookingDto).collect(Collectors.toList());
    }
}
