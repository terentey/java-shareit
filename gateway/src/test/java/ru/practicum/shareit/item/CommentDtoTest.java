package ru.practicum.shareit.item;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import static org.junit.jupiter.api.Assertions.assertEquals;

@JsonTest
class CommentDtoTest {
    @Autowired
    private JacksonTester<CommentDto> json;

    @SneakyThrows
    @Test
    void testCommentDto() {
        String comment = "{\"text\":\"text\"}";

        CommentDto result = json.parseObject(comment);

        assertEquals("text", result.getText());
    }
}