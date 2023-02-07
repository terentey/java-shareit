package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService service;
    private final UserService userService;

    @PostMapping()
    public ItemDto save(@RequestBody @Valid Item item, @RequestHeader(name = "X-Sharer-User-Id") long userId) {
        userService.findById(userId);
        return service.save(item, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestBody Map<String, String> patch,
                          @PathVariable(name = "itemId") long id,
                          @RequestHeader(name = "X-Sharer-User-Id") long userId) {
        userService.findById(userId);
        return service.update(patch, id, userId);
    }

    @GetMapping("/{itemId}")
    public ItemDto findById(@PathVariable(name = "itemId") long id,
                            @RequestHeader(name = "X-Sharer-User-Id") long userId) {
        return service.findById(id, userId);
    }

    @GetMapping()
    public List<ItemDto> findAll(@RequestHeader(name = "X-Sharer-User-Id") long userId) {
        return service.findAll(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestHeader(name = "X-Sharer-User-Id") long userId, @RequestParam(required = false) String text) {
        return service.search(userId, text);
    }
}
