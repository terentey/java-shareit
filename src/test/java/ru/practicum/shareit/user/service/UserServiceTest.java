package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.dto.UserDto;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@SpringBootTest
class UserServiceTest {
    @Autowired
    private UserService service;

    @Test
    @Sql("classpath:test.sql")
    void update() {
        UserDto update = UserDto
                .builder()
                .name("update")
                .build();

        UserDto result = service.update(update, 1);

        assertEquals(update.getName(), result.getName());
    }
}