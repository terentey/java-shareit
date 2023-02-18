package ru.practicum.shareit.user.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.util.Marker;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class UserDto {
    Long id;
    @NotBlank(groups = {Marker.OnCreate.class})
    String name;
    @NotEmpty(groups = {Marker.OnCreate.class})
    @Email(groups = {Marker.OnCreate.class, Marker.OnUpdate.class})
    String email;
}
