package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
class UserControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService service;

    private UserDto dto;

    @BeforeEach
    void beforeEach() {
        dto = UserDto
                .builder()
                .id(1L)
                .name("name")
                .email("email@mail.com")
                .build();
    }

    @SneakyThrows
    @Test
    void save() {
        when(service.save(dto)).thenReturn(dto);

        String result = mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(service, times(1)).save(dto);
        assertEquals(result, objectMapper.writeValueAsString(dto));
    }

    @SneakyThrows
    @Test
    void update() {
        when(service.update(eq(dto), anyLong())).thenReturn(dto);

        String result = mockMvc.perform(patch("/users/{userId}", 1L)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(service, times(1)).update(eq(dto), anyLong());
        assertEquals(result, objectMapper.writeValueAsString(dto));
    }

    @SneakyThrows
    @Test
    void findAll() {
        when(service.findAll()).thenReturn(List.of(dto));

        String result = mockMvc.perform(get("/users")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(service, times(1)).findAll();
        assertEquals(result, objectMapper.writeValueAsString(List.of(dto)));
    }

    @SneakyThrows
    @Test
    void findById() {
        when(service.findById(anyLong())).thenReturn(dto);

        String result = mockMvc.perform(get("/users/{userId}", 1L)
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(service, times(1)).findById(anyLong());
        assertEquals(result, objectMapper.writeValueAsString(dto));
    }

    @SneakyThrows
    @Test
    void deleteTest() {
        doNothing().when(service).delete(anyLong());

        mockMvc.perform(delete("/users/{userId}", 1L)
                        .contentType("application/json"))
                .andExpect(status().isOk());

        verify(service, times(1)).delete(anyLong());
    }
}