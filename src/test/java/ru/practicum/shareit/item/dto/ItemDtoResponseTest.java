package ru.practicum.shareit.item.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemDtoResponseTest {
    @Autowired
    private JacksonTester<ItemDtoResponse> json;

    @SneakyThrows
    @Test
    void testItemDto() {
        ItemDtoResponse dto = ItemDtoResponse
                .builder()
                .id(1L)
                .name("item")
                .description("item_des")
                .available(true)
                .comments(Collections.emptyList())
                .requestId(1L)
                .build();

        JsonContent<ItemDtoResponse> result = json.write(dto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("item");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("item_des");
        assertThat(result).extractingJsonPathBooleanValue("$.available").isTrue();
        assertThat(result).extractingJsonPathValue("$.comments").isEqualTo(Collections.emptyList());
        assertThat(result).extractingJsonPathValue("$.lastBooking").isNull();
        assertThat(result).extractingJsonPathValue("$.nextBooking").isNull();
        assertThat(result).extractingJsonPathNumberValue("$.requestId").isEqualTo(1);
    }
}