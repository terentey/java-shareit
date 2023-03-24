package ru.practicum.shareit.booking.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class BookingDtoResponseTest {
    @Autowired
    private JacksonTester<BookingDtoResponse> json;

    @SneakyThrows
    @Test
    void testBookingDto() {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusDays(1);
        BookingDtoResponse.Booker booker = new BookingDtoResponse.Booker(1L, "booker");
        BookingDtoResponse.Item item = new BookingDtoResponse.Item(1L, "item");
        BookingDtoResponse dto = BookingDtoResponse
                .builder()
                .id(1L)
                .start(start)
                .end(end)
                .status(Status.APPROVED)
                .booker(booker)
                .item(item)
                .build();

        JsonContent<BookingDtoResponse> result = json.write(dto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathValue("$.start").isEqualTo(start.format(formatter));
        assertThat(result).extractingJsonPathValue("$.end").isEqualTo(end.format(formatter));
        assertThat(result).extractingJsonPathValue("$.booker").isNotNull();
        assertThat(result).extractingJsonPathValue("$.item").isNotNull();
    }
}