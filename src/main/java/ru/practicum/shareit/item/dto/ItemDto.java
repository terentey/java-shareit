package ru.practicum.shareit.item.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.util.Marker;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ItemDto {
    Long id;
    @NotBlank(groups = {Marker.OnCreate.class})
    String name;
    @NotBlank(groups = {Marker.OnCreate.class})
    String description;
    @NotNull(groups = {Marker.OnCreate.class})
    Boolean available;
    Set<CommentDto> comments;
    BookingDto lastBooking;
    BookingDto nextBooking;
}
