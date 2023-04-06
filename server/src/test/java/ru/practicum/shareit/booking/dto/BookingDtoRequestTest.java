package ru.practicum.shareit.booking.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@JsonTest
class BookingDtoRequestTest {
    @Autowired
    private JacksonTester<BookingDtoRequest> json;

    @SneakyThrows
    @Test
    void testBookingDto() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusDays(1);
        String booking = String.format("{\"itemId\":\"1\", \"start\":\"%s\", \"end\":\"%s\"}", start, end);

        BookingDtoRequest result = json.parseObject(booking);

        assertEquals(1, result.getItemId());
        assertEquals(start, result.getStart());
        assertEquals(end, result.getEnd());
    }
}