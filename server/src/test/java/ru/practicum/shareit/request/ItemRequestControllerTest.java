package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.dto.ItemRequestDtoRequest;
import ru.practicum.shareit.request.dto.ItemRequestDtoResponse;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
    private ItemRequestService service;

    private ItemRequestDtoRequest requestDtoRequest;
    private ItemRequestDtoResponse requestDtoResponse;

    @BeforeEach
    void beforeEach() {
        requestDtoRequest = new ItemRequestDtoRequest();
        requestDtoResponse = ItemRequestDtoResponse.builder().build();
    }

    @SneakyThrows
    @Test
    void save() {
        requestDtoRequest.setDescription("description");
        when(service.save(requestDtoRequest, 1L)).thenReturn(requestDtoResponse);

        String result = mockMvc.perform(post("/requests")
                        .content(objectMapper.writeValueAsString(requestDtoRequest))
                        .header("X-Sharer-User-Id", 1L)
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(service, times(1)).save(requestDtoRequest, 1L);
        assertEquals(objectMapper.writeValueAsString(requestDtoResponse), result);
    }

    @SneakyThrows
    @Test
    void findById() {
        requestDtoResponse.setId(1L);
        when(service.findById(1L, 1L)).thenReturn(requestDtoResponse);

        String result = mockMvc.perform(get("/requests/1")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(service, times(1)).findById(1L, 1L);
        assertEquals(objectMapper.writeValueAsString(requestDtoResponse), result);
    }

    @SneakyThrows
    @Test
    void findAll_whenByOwnerId() {
        requestDtoResponse.setId(1L);
        when(service.findAllByOwner(1L)).thenReturn(List.of(requestDtoResponse));

        String result = mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(service, times(1)).findAllByOwner(1L);
        assertEquals(objectMapper.writeValueAsString(List.of(requestDtoResponse)), result);
    }

    @SneakyThrows
    @Test
    void findAll() {
        requestDtoResponse.setId(1L);
        when(service.findAll(0, 1, 1L)).thenReturn(List.of(requestDtoResponse));

        String result = mockMvc.perform(get("/requests/all")
                        .param("from", "0")
                        .param("size", "1")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(service, times(1)).findAll(0, 1, 1L);
        assertEquals(objectMapper.writeValueAsString(List.of(requestDtoResponse)), result);
    }
}