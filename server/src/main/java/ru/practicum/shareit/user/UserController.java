package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @PostMapping
    public UserDto save(@RequestBody UserDto userDto) {
        return service.save(userDto);
    }

    @PatchMapping("/{userId}")
    public UserDto update(@RequestBody UserDto userDto,
                          @PathVariable(name = "userId") long id) {
        return service.update(userDto, id);
    }

    @GetMapping()
    public List<UserDto> findAll() {
        return service.findAll();
    }

    @GetMapping("/{userId}")
    public UserDto findById(@PathVariable(name = "userId") long id) {
        return service.findById(id);
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable(name = "userId") long id) {
        service.delete(id);
    }
}
