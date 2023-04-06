package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.util.Marker;

@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserClient client;

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Validated(Marker.OnCreate.class) UserDto userDto) {
        return client.save(userDto);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> update(@RequestBody @Validated(Marker.OnUpdate.class) UserDto userDto,
                                         @PathVariable(name = "userId") long id) {
        return client.update(userDto, id);
    }

    @GetMapping()
    public ResponseEntity<Object> findAll() {
        return client.findAll();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> findById(@PathVariable(name = "userId") long id) {
        return client.findById(id);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> delete(@PathVariable(name = "userId") long id) {
        return client.deleteById(id);
    }
}
