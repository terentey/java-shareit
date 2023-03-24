package ru.practicum.shareit.request.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import static org.junit.jupiter.api.Assertions.assertEquals;

@JsonTest
class ItemRequestDtoRequestTest {
    @Autowired
    private JacksonTester<ItemRequestDtoRequest> json;

    @SneakyThrows
    @Test
    void testRequestDto() {
        String request = "{\"description\":\"description\"}";

        ItemRequestDtoRequest result = json.parseObject(request);

        assertEquals("description", result.getDescription());
    }
}