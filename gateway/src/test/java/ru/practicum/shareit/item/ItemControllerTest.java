package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
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
    private ItemClient client;

    private ItemDto itemDto;
    private CommentDto commentDto;

    @BeforeEach
    void beforeEach() {
        itemDto = ItemDto
                .builder()
                .id(1L)
                .name("name")
                .description("description")
                .available(true)
                .build();
        commentDto = CommentDto
                .builder()
                .id(1L)
                .text("text")
                .build();
    }

    @SneakyThrows
    @Test
    void save_whenItemDtoIsValid_thenReturnStatusIsOk() {
        when(client.save(eq(itemDto), anyLong())).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        mockMvc.perform(post("/items")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(itemDto))
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());

        verify(client, times(1)).save(eq(itemDto), anyLong());
    }

    @SneakyThrows
    @Test
    void save_whenNameIsNull_thenReturnStatusIsBadRequest() {
        itemDto.setName(null);

        mockMvc.perform(post("/items")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(itemDto))
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isBadRequest());

        verify(client, never()).save(eq(itemDto), anyLong());
    }

    @SneakyThrows
    @Test
    void save_whenNameIsBlank_thenReturnStatusIsBadRequest() {
        itemDto.setName("    ");

        mockMvc.perform(post("/items")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(itemDto))
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isBadRequest());

        verify(client, never()).save(eq(itemDto), anyLong());
    }

    @SneakyThrows
    @Test
    void save_whenDescriptionIsNull_thenReturnStatusIsBadRequest() {
        itemDto.setDescription(null);

        mockMvc.perform(post("/items")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(itemDto))
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isBadRequest());

        verify(client, never()).save(eq(itemDto), anyLong());
    }

    @SneakyThrows
    @Test
    void save_whenDescriptionIsBlank_thenReturnStatusIsBadRequest() {
        itemDto.setName("    ");

        mockMvc.perform(post("/items")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(itemDto))
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isBadRequest());

        verify(client, never()).save(eq(itemDto), anyLong());
    }

    @SneakyThrows
    @Test
    void save_whenAvailableIsNull_thenReturnStatusIsBadRequest() {
        itemDto.setAvailable(null);

        mockMvc.perform(post("/items")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(itemDto))
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isBadRequest());

        verify(client, never()).save(eq(itemDto), anyLong());
    }

    @SneakyThrows
    @Test
    void saveComment_whenCommentDtoIsValid_thenReturnStatusIsOk() {
        when(client.saveComment(eq(commentDto), anyLong(), anyInt())).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        mockMvc.perform(post("/items/{itemId}/comment", 1L)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(commentDto))
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());

        verify(client, times(1)).saveComment(eq(commentDto), anyLong(), anyLong());
    }

    @SneakyThrows
    @Test
    void saveComment_whenTextIsNull_thenReturnStatusIsBadRequest() {
        commentDto.setText(null);

        mockMvc.perform(post("/items/{itemId}/comment", 1L)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(commentDto))
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isBadRequest());

        verify(client, never()).saveComment(eq(commentDto), anyLong(), anyLong());
    }

    @SneakyThrows
    @Test
    void saveComment_whenTextIsBlank_thenReturnStatusIsBadRequest() {
        commentDto.setText("    ");

        mockMvc.perform(post("/items/{itemId}/comment", 1L)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(commentDto))
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isBadRequest());

        verify(client, never()).saveComment(eq(commentDto), anyLong(), anyLong());
    }

    @SneakyThrows
    @Test
    void update() {
        when(client.update(eq(itemDto), anyLong(), anyLong())).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        mockMvc.perform(patch("/items/{itemId}", 1L)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(itemDto))
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());

        verify(client, times(1)).update(eq(itemDto), anyLong(), anyLong());
    }

    @SneakyThrows
    @Test
    void findById() {
        when(client.findById(anyLong(), anyLong())).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        mockMvc.perform(get("/items/{itemId}", 1L)
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());

        verify(client, times(1)).findById(anyLong(), anyLong());
    }

    @SneakyThrows
    @Test
    void findAll_whenAllArgumentsIsNotNullAndCorrect_thenReturnIsOk() {
        when(client.findAll(anyLong(), anyInt(), anyInt())).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        mockMvc.perform(get("/items")
                        .contentType("application/json")
                        .param("from", "0")
                        .param("size", "1")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());

        verify(client, times(1)).findAll(anyLong(), anyInt(), anyInt());
    }

    @SneakyThrows
    @Test
    void findAll_whenFromIsNull_thenReturnIsOk() {
        when(client.findAll(anyLong(), anyInt(), anyInt())).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        mockMvc.perform(get("/items")
                        .contentType("application/json")
                        .param("size", "1")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());

        verify(client, times(1)).findAll(anyLong(), anyInt(), anyInt());
    }

    @SneakyThrows
    @Test
    void findAll_whenSizeIsNull_thenReturnIsOk() {
        when(client.findAll(anyLong(), anyInt(), anyInt())).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        mockMvc.perform(get("/items")
                        .contentType("application/json")
                        .param("from", "1")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());

        verify(client, times(1)).findAll(anyLong(), anyInt(), anyInt());
    }

    @SneakyThrows
    @Test
    void findAll_whenFromIsNotPositive_thenReturnStatusIsInternalServerError() {
        mockMvc.perform(get("/items")
                        .contentType("application/json")
                        .param("from", "-1")
                        .param("size", "1")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isInternalServerError());

        verify(client, never()).findAll(anyLong(), anyInt(), anyInt());
    }

    @SneakyThrows
    @Test
    void findAll_whenSizeIsNotPositive_thenReturnStatusIsInternalServerError() {
        mockMvc.perform(get("/items")
                        .contentType("application/json")
                        .param("from", "1")
                        .param("size", "-1")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isInternalServerError());

        verify(client, never()).findAll(anyLong(), anyInt(), anyInt());
    }

    @SneakyThrows
    @Test
    void search_whenAllArgumentsIsNotNullAndCorrect_thenReturnIsOk() {
        when(client.search(anyLong(), anyString(), anyInt(), anyInt())).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        mockMvc.perform(get("/items/search")
                        .contentType("application/json")
                        .param("text", "search")
                        .param("from", "0")
                        .param("size", "1")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());

        verify(client, times(1)).search(anyLong(), anyString(), anyInt(), anyInt());
    }

    @SneakyThrows
    @Test
    void search_whenFromIsNull_thenReturnIsOk() {
        when(client.search(anyLong(), anyString(), anyInt(), anyInt())).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        mockMvc.perform(get("/items/search")
                        .contentType("application/json")
                        .param("text", "search")
                        .param("size", "1")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());

        verify(client, times(1)).search(anyLong(), anyString(), anyInt(), anyInt());
    }

    @SneakyThrows
    @Test
    void search_whenSizeIsNull_thenReturnIsOk() {
        when(client.search(anyLong(), anyString(), anyInt(), anyInt())).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        mockMvc.perform(get("/items/search")
                        .contentType("application/json")
                        .param("text", "search")
                        .param("from", "1")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());

        verify(client, times(1)).search(anyLong(), anyString(), anyInt(), anyInt());
    }

    @SneakyThrows
    @Test
    void search_whenFromIsNotPositive_thenReturnStatusIsInternalServerError() {
        mockMvc.perform(get("/items/search")
                        .contentType("application/json")
                        .param("text", "search")
                        .param("from", "-1")
                        .param("size", "1")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isInternalServerError());

        verify(client, never()).search(anyLong(), anyString(), anyInt(), anyInt());
    }

    @SneakyThrows
    @Test
    void search_whenSizeIsNotPositive_thenReturnStatusIsInternalServerError() {
        mockMvc.perform(get("/items/search")
                        .contentType("application/json")
                        .param("text", "search")
                        .param("from", "1")
                        .param("size", "-1")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isInternalServerError());

        verify(client, never()).search(anyLong(), anyString(), anyInt(), anyInt());
    }
}