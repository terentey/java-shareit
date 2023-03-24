package ru.practicum.shareit.booking;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.annotation.EndAfterStart;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@EndAfterStart
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class BookingDto {
    Long id;
    @NotNull
    @Positive
    Long itemId;
    @FutureOrPresent
    LocalDateTime start;
    LocalDateTime end;
}