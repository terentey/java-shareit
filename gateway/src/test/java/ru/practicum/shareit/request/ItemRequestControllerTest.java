package ru.practicum.shareit.request;

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

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
class ItemRequestControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemRequestClient client;

    private ItemRequestDto requestDto;

    @BeforeEach
    void beforeEach() {
        requestDto = new ItemRequestDto();
    }

    @SneakyThrows
    @Test
    void save_whenRequestDtoIsValid_thenReturnStatusIsOk() {
        requestDto.setDescription("request");
        when(client.save(eq(requestDto), anyLong())).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        mockMvc.perform(post("/requests")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(requestDto))
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());

        verify(client, times(1)).save(eq(requestDto), anyLong());
    }

    @SneakyThrows
    @Test
    void save_whenItemDtoIsNotValid_thenReturnIStatusIsBadRequest() {
        when(client.save(eq(requestDto), anyLong())).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        mockMvc.perform(post("/requests")
                        .content(objectMapper.writeValueAsString(requestDto))
                        .header("X-Sharer-User-Id", 1L)
                        .contentType("application/json"))
                .andExpect(status().isBadRequest());

        verify(client, never()).save(eq(requestDto), anyLong());
    }

    @SneakyThrows
    @Test
    void findById() {
        when(client.findById(anyLong(), anyLong())).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        mockMvc.perform(get("/requests/{requestId}", 1L)
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());

        verify(client, times(1)).findById(anyLong(), anyLong());
    }

    @SneakyThrows
    @Test
    void findAllByOwnerId() {
        when(client.findAllByOwner(anyLong())).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        mockMvc.perform(get("/requests")
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());

        verify(client, times(1)).findAllByOwner(anyLong());
    }

    @SneakyThrows
    @Test
    void findAll_thenAllArgumentsIsNotNullAnCorrect_thenReturnStatusIsOk() {
        when(client.findAll(anyInt(), anyInt(), anyLong())).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        mockMvc.perform(get("/requests/all")
                        .param("from", "0")
                        .param("size", "1")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType("application/json"))
                .andExpect(status().isOk());

        verify(client, times(1)).findAll(anyInt(), anyInt(), anyLong());
    }

    @SneakyThrows
    @Test
    void findAll_whenFromIsNull_thenReturnListOfItemRequest() {
        when(client.findAll(anyInt(), anyInt(), anyLong())).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        mockMvc.perform(get("/requests/all")
                        .param("size", "1")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType("application/json"))
                .andExpect(status().isOk());

        verify(client, times(1)).findAll(anyInt(), anyInt(), anyLong());
    }

    @SneakyThrows
    @Test
    void findAll_whenSizeIsNull_thenReturnListOfItemRequest() {
        when(client.findAll(anyInt(), anyInt(), anyLong())).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        mockMvc.perform(get("/requests/all")
                        .param("from", "0")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType("application/json"))
                .andExpect(status().isOk());

        verify(client, times(1)).findAll(anyInt(), anyInt(), anyLong());
    }

    @SneakyThrows
    @Test
    void findAll_whenFromIsNotPositiveOrZero_thenReturnStatusIsBadRequest() {
        mockMvc.perform(get("/requests/all")
                        .param("from", "-1")
                        .param("size", "1")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType("application/json"))
                .andExpect(status().isInternalServerError())
                .andReturn()
                .getResponse();

        verify(client, never()).findAll(anyInt(), anyInt(), anyLong());
    }

    @SneakyThrows
    @Test
    void findAll_whenSizeIsNotPositive_thenReturnStatusIsBadRequest() {
        mockMvc.perform(get("/requests/all")
                        .param("from", "1")
                        .param("size", "-1")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType("application/json"))
                .andExpect(status().isInternalServerError())
                .andReturn()
                .getResponse();

        verify(client, never()).findAll(anyInt(), anyInt(), anyLong());
    }
}