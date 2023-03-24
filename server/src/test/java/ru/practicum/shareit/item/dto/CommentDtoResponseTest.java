package ru.practicum.shareit.item.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class CommentDtoResponseTest {
    @Autowired
    private JacksonTester<CommentDtoResponse> json;

    @SneakyThrows
    @Test
    void testCommentDto() {
        Instant created = Instant.now();
        CommentDtoResponse dto = CommentDtoResponse
                .builder()
                .id(1L)
                .text("text")
                .authorName("user")
                .created(created)
                .build();

        JsonContent<CommentDtoResponse> result = json.write(dto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.text").isEqualTo("text");
        assertThat(result).extractingJsonPathStringValue("$.authorName").isEqualTo("user");
        assertThat(result).extractingJsonPathValue("$.created").isEqualTo(created.toString());
    }
}