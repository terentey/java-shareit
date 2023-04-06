package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.service.BookingService;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
class BookingControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingService service;

    private BookingDtoRequest request;
    private BookingDtoResponse response;

    @BeforeEach
    void beforeEach() {
        request = BookingDtoRequest
                .builder()
                .id(1L)
                .itemId(1L)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();
        response = BookingDtoResponse
                .builder()
                .id(1L)
                .build();
    }

    @SneakyThrows
    @Test
    void save() {
        when(service.save(eq(request), anyLong())).thenReturn(response);

        String result = mockMvc.perform(post("/bookings")
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
    void update() {
        when(service.update(anyLong(), anyLong(), anyBoolean())).thenReturn(response);

        String result = mockMvc.perform(patch("/bookings/{bookingId}", 1L)
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", 1L)
                        .param("approved", "true"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(service, times(1)).update(anyLong(), anyLong(), anyBoolean());
        assertEquals(objectMapper.writeValueAsString(response), result);
    }

    @SneakyThrows
    @Test
    void findByUserId() {
        when(service.findAllByUserId(anyLong(), anyString(), anyInt(), anyInt())).thenReturn(List.of(response));

        String result = mockMvc.perform(get("/bookings")
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", "REJECTED")
                        .param("from", "1")
                        .param("size", "1"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(service, times(1)).findAllByUserId(anyLong(), anyString(), anyInt(), anyInt());
        assertEquals(objectMapper.writeValueAsString(List.of(response)), result);
    }

    @SneakyThrows
    @Test
    void findByOwnerId() {
        when(service.findAllByOwnerId(anyLong(), anyString(), anyInt(), anyInt())).thenReturn(List.of(response));

        String result = mockMvc.perform(get("/bookings/owner")
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", "REJECTED")
                        .param("from", "1")
                        .param("size", "1"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(service, times(1)).findAllByOwnerId(anyLong(), anyString(), anyInt(), anyInt());
        assertEquals(objectMapper.writeValueAsString(List.of(response)), result);
    }
}