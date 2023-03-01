package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.util.Marker;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService service;

    @PostMapping()
    public ItemDto save(@RequestBody @Validated(Marker.OnCreate.class) ItemDto itemDto,
                        @RequestHeader(name = "X-Sharer-User-Id") long userId) {
        return service.save(itemDto, userId);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto saveComment(@RequestBody @Validated(Marker.OnCreate.class) CommentDto commentDto,
                                  @PathVariable long itemId,
                                  @RequestHeader(name = "X-Sharer-User-Id") long userId) {
        return service.saveComment(commentDto, itemId, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestBody ItemDto itemDto,
                          @PathVariable(name = "itemId") long id,
                          @RequestHeader(name = "X-Sharer-User-Id") long userId) {
        return service.update(itemDto, id, userId);
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
    public List<ItemDto> search(@RequestHeader(name = "X-Sharer-User-Id") long userId, @RequestParam String text) {
        return service.search(userId, text);
    }
}
