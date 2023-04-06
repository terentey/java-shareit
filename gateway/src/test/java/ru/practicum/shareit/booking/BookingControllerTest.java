package ru.practicum.shareit.booking;

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

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.anyBoolean;
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
    private BookingClient client;

    private BookingDto request;

    @BeforeEach
    void beforeEach() {
        request = BookingDto
                .builder()
                .id(1L)
                .itemId(1L)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();
    }

    @SneakyThrows
    @Test
    void save_whenBookingDtoIsValid_thenReturnStatusIsOk() {
        when(client.save(eq(request), anyLong())).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        mockMvc.perform(post("/bookings")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());

        verify(client, times(1)).save(eq(request), anyLong());
    }

    @SneakyThrows
    @Test
    void save_whenItemIdIsNull_thenReturnStatusIsBadRequest() {
        request.setItemId(null);

        mockMvc.perform(post("/bookings")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isBadRequest());

        verify(client, never()).save(eq(request), anyLong());
    }

    @SneakyThrows
    @Test
    void save_whenItemIdIsNotPositive_thenReturnStatusIsBadRequest() {
        request.setItemId(-1L);

        mockMvc.perform(post("/bookings")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isBadRequest());

        verify(client, never()).save(eq(request), anyLong());
    }

    @SneakyThrows
    @Test
    void save_whenEndAfterStart_thenReturnStatusIsBadRequest() {
        request.setStart(LocalDateTime.now().plusDays(2));
        request.setEnd(LocalDateTime.now().plusDays(1));

        mockMvc.perform(post("/bookings")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isBadRequest());

        verify(client, never()).save(eq(request), anyLong());
    }

    @SneakyThrows
    @Test
    void save_whenStartIsPast_thenReturnStatusIsBadRequest() {
        request.setStart(LocalDateTime.now().minusDays(2));

        mockMvc.perform(post("/bookings")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isBadRequest());

        verify(client, never()).save(eq(request), anyLong());
    }

    @SneakyThrows
    @Test
    void save_whenEndIsPast_thenReturnStatusIsBadRequest() {
        request.setEnd(LocalDateTime.now().minusDays(2));

        mockMvc.perform(post("/bookings")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isBadRequest());

        verify(client, never()).save(eq(request), anyLong());
    }

    @SneakyThrows
    @Test
    void update() {
        when(client.update(anyLong(), anyLong(), anyBoolean())).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        mockMvc.perform(patch("/bookings/{bookingId}", 1L)
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", 1L)
                        .param("approved", "true"))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void findById() {
        when(client.findById(anyLong(), anyLong())).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        mockMvc.perform(get("/bookings/{bookingId}", 1L)
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());

        verify(client, times(1)).findById(anyLong(), anyLong());
    }

    @SneakyThrows
    @Test
    void findByUserId_whenAllArgumentsIsNotNullAndCorrect_thenReturnStatusIsOk() {
        when(client.findAllByUserId(anyLong(), anyString(), anyInt(), anyInt()))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        mockMvc.perform(get("/bookings")
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", "REJECTED")
                        .param("from", "0")
                        .param("size", "1"))
                .andExpect(status().isOk());

        verify(client, times(1)).findAllByUserId(anyLong(), anyString(), anyInt(), anyInt());
    }

    @SneakyThrows
    @Test
    void findByUserId_whenStateIsNull_thenReturnStatusIsOk() {
        when(client.findAllByUserId(anyLong(), eq("ALL"), anyInt(), anyInt()))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        mockMvc.perform(get("/bookings")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-Sharer-User-Id", 1L)
                        .param("from", "1")
                        .param("size", "1"))
                .andExpect(status().isOk());

        verify(client, times(1)).findAllByUserId(anyLong(), eq("ALL"), anyInt(), anyInt());
    }

    @SneakyThrows
    @Test
    void findByUserId_whenFromIsNull_thenReturnStatusIsOk() {
        when(client.findAllByUserId(anyLong(), anyString(), eq(0), anyInt()))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        mockMvc.perform(get("/bookings")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", "ALL")
                        .param("size", "1"))
                .andExpect(status().isOk());

        verify(client, times(1)).findAllByUserId(anyLong(), anyString(), eq(0), anyInt());
    }

    @SneakyThrows
    @Test
    void findByUserId_whenSizeIsNull_thenReturnStatusIsOk() {
        when(client.findAllByUserId(anyLong(), anyString(), anyInt(), eq(20)))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        mockMvc.perform(get("/bookings")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", "ALL")
                        .param("from", "0"))
                .andExpect(status().isOk());

        verify(client, times(1)).findAllByUserId(anyLong(), anyString(), anyInt(), eq(20));
    }

    @SneakyThrows
    @Test
    void findByUserId_whenFromIsNotPositive_thenReturnStatusIsInternalServerError() {
        mockMvc.perform(get("/bookings")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", "REJECTED")
                        .param("from", "-1")
                        .param("size", "1"))
                .andExpect(status().isInternalServerError());

        verify(client, never()).findAllByUserId(anyLong(), anyString(), anyInt(), anyInt());
    }

    @SneakyThrows
    @Test
    void findByUserId_whenSizeIsNotPositive_thenReturnStatusIsInternalServerError() {
        mockMvc.perform(get("/bookings")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", "REJECTED")
                        .param("from", "0")
                        .param("size", "-1"))
                .andExpect(status().isInternalServerError());

        verify(client, never()).findAllByUserId(anyLong(), anyString(), anyInt(), anyInt());
    }

    @SneakyThrows
    @Test
    void findByOwnerId_whenAllArgumentsIsNotNullAndCorrect_thenReturnStatusIsOk() {
        when(client.findAllByOwnerId(anyLong(), eq("ALL"), anyInt(), anyInt()))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        mockMvc.perform(get("/bookings/owner")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", "ALL")
                        .param("from", "1")
                        .param("size", "1"))
                .andExpect(status().isOk());

        verify(client, times(1)).findAllByOwnerId(anyLong(), eq("ALL"), anyInt(), anyInt());
    }

    @SneakyThrows
    @Test
    void findByOwnerId_whenStateIsNull_thenReturnStatusIsOk() {
        when(client.findAllByOwnerId(anyLong(), eq("ALL"), anyInt(), anyInt()))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        mockMvc.perform(get("/bookings/owner")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-Sharer-User-Id", 1L)
                        .param("from", "1")
                        .param("size", "1"))
                .andExpect(status().isOk());

        verify(client, times(1)).findAllByOwnerId(anyLong(), eq("ALL"), anyInt(), anyInt());
    }

    @SneakyThrows
    @Test
    void findByOwnerId_whenFromIsNull_thenReturnStatusIsOk() {
        when(client.findAllByOwnerId(anyLong(), anyString(), eq(0), anyInt()))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        mockMvc.perform(get("/bookings/owner")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", "ALL")
                        .param("size", "1"))
                .andExpect(status().isOk());

        verify(client, times(1)).findAllByOwnerId(anyLong(), anyString(), eq(0), anyInt());
    }

    @SneakyThrows
    @Test
    void findByOwnerId_whenSizeIsNull_thenReturnStatusIsOk() {
        when(client.findAllByOwnerId(anyLong(), anyString(), anyInt(), eq(20)))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        mockMvc.perform(get("/bookings/owner")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", "ALL")
                        .param("from", "0"))
                .andExpect(status().isOk());

        verify(client, times(1)).findAllByOwnerId(anyLong(), anyString(), anyInt(), eq(20));
    }

    @SneakyThrows
    @Test
    void findByOwnerId_whenFromIsNotPositive_thenReturnStatusIsInternalServerError() {
        mockMvc.perform(get("/bookings/owner")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", "REJECTED")
                        .param("from", "-1")
                        .param("size", "1"))
                .andExpect(status().isInternalServerError());

        verify(client, never()).findAllByOwnerId(anyLong(), anyString(), anyInt(), anyInt());
    }

    @SneakyThrows
    @Test
    void findByOwnerId_whenSizeIsNotPositive_thenReturnStatusIsInternalServerError() {
        mockMvc.perform(get("/bookings/owner")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", "REJECTED")
                        .param("from", "0")
                        .param("size", "-1"))
                .andExpect(status().isInternalServerError());

        verify(client, never()).findAllByOwnerId(anyLong(), anyString(), anyInt(), anyInt());
    }
}