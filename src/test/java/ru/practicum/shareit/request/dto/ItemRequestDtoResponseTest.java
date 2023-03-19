package ru.practicum.shareit.request.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemRequestDtoResponseTest {
    @Autowired
    private JacksonTester<ItemRequestDtoResponse> json;

    @SneakyThrows
    @Test
    void testItemRequestDto() {
        LocalDateTime created = LocalDateTime.now();
        ItemRequestDtoResponse dto = ItemRequestDtoResponse
                .builder()
                .id(1L)
                .description("request")
                .created(created)
                .items(Collections.emptyList())
                .build();

        JsonContent<ItemRequestDtoResponse> result = json.write(dto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("request");
        assertThat(result).extractingJsonPathValue("$.created").isEqualTo(created.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        assertThat(result).extractingJsonPathValue("$.items").isEqualTo(Collections.emptyList());
    }
}