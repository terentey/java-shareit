package ru.practicum.shareit.user;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

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
}