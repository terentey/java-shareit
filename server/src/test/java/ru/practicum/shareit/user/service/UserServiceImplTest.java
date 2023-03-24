package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.when;
import static ru.practicum.shareit.util.UserTest.getNewUser;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl service;

    @Mock
    UserRepository repo;

    private User user;
    private UserDto userDto;

    @BeforeEach
    void beforeEach() {
        user = getNewUser(1L, "name", "email@mail.com");
        userDto = UserDto
                .builder()
                .id(1L)
                .name("update_name")
                .email("update_email@mail.com")
                .build();
    }

    @Test
    void update_whenUpdatedNameAndEmail_thenReturnUserWithUpdatedNameAndUser() {
        when(repo.findById(1L)).thenReturn(Optional.of(user));

        UserDto result = service.update(userDto, 1L);

        assertEquals(userDto, result);
        assertEquals(userDto.getName(), result.getName());
        assertEquals(userDto.getEmail(), result.getEmail());
    }

    @Test
    void update_whenUpdatedNameAndEmailIsNull_thenReturnUserWithUpdatedName() {
        userDto.setEmail(null);
        when(repo.findById(1L)).thenReturn(Optional.of(user));

        UserDto result = service.update(userDto, 1L);

        assertNotEquals(userDto, result);
        assertEquals(userDto.getName(), result.getName());
        assertEquals(user.getEmail(), result.getEmail());
    }

    @Test
    void update_whenUpdatedNameAndEmailIsBlank_thenReturnUserWithUpdatedName() {
        userDto.setEmail("  ");
        when(repo.findById(1L)).thenReturn(Optional.of(user));

        UserDto result = service.update(userDto, 1L);

        assertNotEquals(userDto, result);
        assertEquals(userDto.getName(), result.getName());
        assertEquals(user.getEmail(), result.getEmail());
    }

    @Test
    void update_whenUpdatedEmailAndNameIsNull_thenReturnUserWithUpdatedEmail() {
        userDto.setName(null);
        when(repo.findById(1L)).thenReturn(Optional.of(user));

        UserDto result = service.update(userDto, 1L);

        assertNotEquals(userDto, result);
        assertEquals(userDto.getEmail(), result.getEmail());
        assertEquals(user.getName(), result.getName());
    }

    @Test
    void update_whenUpdatedEmailAndNameIsBlank_thenReturnUserWithUpdateEmail() {
        userDto.setName("   ");
        when(repo.findById(1L)).thenReturn(Optional.of(user));

        UserDto result = service.update(userDto, 1L);

        assertNotEquals(userDto, result);
        assertEquals(userDto.getEmail(), result.getEmail());
        assertEquals(user.getName(), result.getName());
    }

    @Test
    void update_whenUpdatedNameIsNullAndUpdatedEmailIsNull_thenReturnNotUpdatedUser() {
        userDto.setName(null);
        userDto.setEmail(null);
        when(repo.findById(1L)).thenReturn(Optional.of(user));

        UserDto result = service.update(userDto, 1L);

        assertNotEquals(userDto, result);
        assertEquals(user.getName(), result.getName());
        assertEquals(user.getEmail(), result.getEmail());
    }

    @Test
    void update_whenUpdatedNameIsBlankAndUpdatedEmailIsBlank_thenReturnNotUpdatedUser() {
        userDto.setName("   ");
        userDto.setEmail("   ");
        when(repo.findById(1L)).thenReturn(Optional.of(user));

        UserDto result = service.update(userDto, 1L);

        assertNotEquals(userDto, result);
        assertEquals(user.getName(), result.getName());
        assertEquals(user.getEmail(), result.getEmail());
    }
}