package ru.practicum.shareit.user;

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

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
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
    private UserClient client;

    private UserDto userDto;

    @BeforeEach
    void beforeEach() {
        userDto = UserDto
                .builder()
                .id(1L)
                .name("name")
                .email("email@mail.com")
                .build();
    }

    @SneakyThrows
    @Test
    void save_whenUserDtoIsValid_thenReturnStatusIsOk() {
        when(client.save(userDto)).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk());

        verify(client, times(1)).save(userDto);
    }

    @SneakyThrows
    @Test
    void save_whenNameIsBlank_thenReturnStatusIsBadRequest() {
        userDto.setName("   ");

        mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isBadRequest());

        verify(client, never()).save(userDto);
    }

    @SneakyThrows
    @Test
    void save_whenEmailIsNotValid_thenReturnStatusIsBadRequest() {
        userDto.setEmail("email");

        mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isBadRequest());

        verify(client, never()).save(userDto);
    }

    @SneakyThrows
    @Test
    void save_whenEmailIsEmpty_thenReturnStatusIsBadRequest() {
        userDto.setEmail("");

        mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isBadRequest());

        verify(client, never()).save(userDto);
    }

    @SneakyThrows
    @Test
    void update_whenUserDtoIsValid_thenReturnStatusIsOk() {
        when(client.update(eq(userDto), anyLong())).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        mockMvc.perform(patch("/users/{userId}", 1L)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk());

        verify(client, times(1)).update(eq(userDto), anyLong());
    }

    @SneakyThrows
    @Test
    void update_whenEmailIsNotValid_thenReturnStatusIsBadRequest() {
        userDto.setEmail("email");

        mockMvc.perform(patch("/users/{userId}", 1L)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isBadRequest());

        verify(client, never()).update(eq(userDto), anyLong());
    }

    @SneakyThrows
    @Test
    void findAll() {
        when(client.findAll()).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        mockMvc.perform(get("/users")).andExpect(status().isOk());

        verify(client, times(1)).findAll();
    }

    @SneakyThrows
    @Test
    void findById() {
        when(client.findById(anyLong())).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        mockMvc.perform(get("/users/{userId}", 1L)).andExpect(status().isOk());

        verify(client, times(1)).findById(anyLong());
    }

    @SneakyThrows
    @Test
    void deleteTest() {
        when(client.deleteById(anyLong())).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        mockMvc.perform(delete("/users/{userId}", 1L)).andExpect(status().isOk());

        verify(client, times(1)).deleteById(anyLong());
    }
}