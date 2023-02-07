package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @PostMapping
    public UserDto save(@RequestBody @Valid User user) {
        return service.save(user);
    }

    @PatchMapping("/{userId}")
    public UserDto update(@RequestBody Map<String, String> patch, @PathVariable(name = "userId") long id) {
        return service.update(patch, id);
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
