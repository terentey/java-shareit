package ru.practicum.shareit.request.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ItemRequestDtoResponse {
    Long id;
    String description;
    LocalDateTime created;
    List<ItemDto> items;

    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
    public static class ItemDto {
        Long id;
        String name;
        String description;
        Boolean available;
        Long requestId;
    }
}
