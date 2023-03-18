package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@SpringBootTest
class BookingServiceTest {
    @Autowired
    private BookingService service;

    @Test
    @Sql("classpath:test.sql")
    void findAllByUser() {
        List<BookingDtoResponse> result = service.findAllByUserId(2, "ALL", 0, null);

        assertEquals(1, result.get(0).getId());
    }
}