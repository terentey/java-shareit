package ru.practicum.shareit.item;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import static org.junit.jupiter.api.Assertions.assertEquals;

@JsonTest
class ItemDtoTest {
    @Autowired
    private JacksonTester<ItemDto> json;

    @SneakyThrows
    @Test
    void testItemDtoRequest() {
        String item = "{\"name\":\"name\", \"description\":\"description\", \"available\":\"true\", \"requestId\":\"1\"}";

        ItemDto result = json.parseObject(item);

        assertEquals("name", result.getName());
        assertEquals("description", result.getDescription());
        assertEquals(true, result.getAvailable());
        assertEquals(1, result.getRequestId());
    }
}