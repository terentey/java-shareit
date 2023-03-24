package ru.practicum.shareit.user.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@JsonTest
class UserDtoTest {
    @Autowired
    private JacksonTester<UserDto> json;

    @SneakyThrows
    @Test
    void testUserDto() {
        String user = "{\"name\":\"user\", \"email\":\"user@user.com\"}";

        UserDto userDto = json.parseObject(user);

        assertEquals("user", userDto.getName());
        assertEquals("user@user.com", userDto.getEmail());
    }

    @SneakyThrows
    @Test
    void testUserDtoSerialization() {
        UserDto userDto = UserDto
                .builder()
                .id(1L)
                .name("user")
                .email("user@usre.com")
                .build();

        JsonContent<UserDto> result = json.write(userDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("user");
        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo("user@usre.com");
    }
}