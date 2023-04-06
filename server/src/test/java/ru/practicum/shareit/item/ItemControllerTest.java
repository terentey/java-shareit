package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.CommentDtoRequest;
import ru.practicum.shareit.item.dto.CommentDtoResponse;
import ru.practicum.shareit.item.dto.ItemDtoRequest;
import ru.practicum.shareit.item.dto.ItemDtoResponse;
import ru.practicum.shareit.item.service.ItemService;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
class ItemControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemService service;

    private ItemDtoRequest request;
    private ItemDtoResponse response;
    private CommentDtoRequest commentDtoRequest;
    private CommentDtoResponse commentDtoResponse;

    @BeforeEach
    void beforeEach() {
        request = ItemDtoRequest
                .builder()
                .id(1L)
                .name("name")
                .description("description")
                .available(true)
                .build();
        response = ItemDtoResponse
                .builder()
                .id(1L)
                .name("name")
                .description("description")
                .available(true)
                .build();
        commentDtoRequest = CommentDtoRequest
                .builder()
                .id(1L)
                .text("text")
                .build();
        commentDtoResponse = CommentDtoResponse
                .builder()
                .id(1L)
                .text("text")
                .authorName("name")
                .created(Instant.now())
                .build();
    }

    @SneakyThrows
    @Test
    void save() {
        when(service.save(eq(request), anyLong())).thenReturn(response);

        String result = mockMvc.perform(post("/items")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(service, times(1)).save(eq(request), anyLong());
        assertEquals(objectMapper.writeValueAsString(response), result);
    }

    @SneakyThrows
    @Test
    void saveComment() {
        when(service.saveComment(eq(commentDtoRequest), anyLong(), anyLong())).thenReturn(commentDtoResponse);

        String result = mockMvc.perform(post("/items/{itemId}/comment", 1L)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(commentDtoRequest))
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(service, times(1)).saveComment(eq(commentDtoRequest), anyLong(), anyLong());
        assertEquals(objectMapper.writeValueAsString(commentDtoResponse), result);
    }


    @SneakyThrows
    @Test
    void update() {
        when(service.update(eq(request), anyLong(), anyLong())).thenReturn(response);

        String result = mockMvc.perform(patch("/items/{itemId}", 1L)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify((service), times(1)).update(eq(request), anyLong(), anyLong());
        assertEquals(objectMapper.writeValueAsString(response), result);
    }

    @SneakyThrows
    @Test
    void findById() {
        when(service.findById(anyLong(), anyLong())).thenReturn(response);

        String result = mockMvc.perform(get("/items/{itemId}", 1L)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(service, times(1)).findById(anyLong(), anyLong());
        assertEquals(objectMapper.writeValueAsString(response), result);
    }

    @SneakyThrows
    @Test
    void findAll() {
        when(service.findAll(anyLong(), anyInt(), anyInt())).thenReturn(List.of(response));

        String result = mockMvc.perform(get("/items")
                        .contentType("application/json")
                        .param("from", "1")
                        .param("size", "1")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(service, times(1)).findAll(anyLong(), anyInt(), anyInt());
        assertEquals(objectMapper.writeValueAsString(List.of(response)), result);
    }

    @SneakyThrows
    @Test
    void search() {
        when(service.search(anyLong(), anyString(), anyInt(), anyInt())).thenReturn(List.of(response));

        String result = mockMvc.perform(get("/items/search")
                        .contentType("application/json")
                        .param("text", "search")
                        .param("from", "1")
                        .param("size", "1")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(service, times(1)).search(anyLong(), anyString(), anyInt(), anyInt());
        assertEquals(objectMapper.writeValueAsString(List.of(response)), result);
    }
}