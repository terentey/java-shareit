package ru.practicum.shareit.booking.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.ItemDtoResponse;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.util.Marker;

import javax.validation.constraints.NotNull;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class BookingDtoResponse {
    Long id;
    Long itemId;
    Long bookerId;
    @NotNull(groups = {Marker.OnCreate.class})
    String start;
    @NotNull(groups = {Marker.OnCreate.class})
    String end;
    Status status;
    UserDto booker;
    ItemDtoResponse item;
}
