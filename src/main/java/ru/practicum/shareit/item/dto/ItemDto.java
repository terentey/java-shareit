package ru.practicum.shareit.item.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.util.Marker;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
}
