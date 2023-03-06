package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDtoRequest;
import ru.practicum.shareit.item.dto.CommentDtoResponse;
import ru.practicum.shareit.item.dto.ItemDtoRequest;
import ru.practicum.shareit.item.dto.ItemDtoResponse;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.util.Marker;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService service;

    @PostMapping()
    public ItemDtoResponse save(@RequestBody @Validated(Marker.OnCreate.class) ItemDtoRequest itemDto,
                                @RequestHeader(name = "X-Sharer-User-Id") long userId) {
        return service.save(itemDto, userId);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDtoResponse saveComment(@RequestBody @Validated() CommentDtoRequest commentDto,
                                          @PathVariable long itemId,
                                          @RequestHeader(name = "X-Sharer-User-Id") long userId) {
        return service.saveComment(commentDto, itemId, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDtoResponse update(@RequestBody ItemDtoRequest itemDto,
                                  @PathVariable(name = "itemId") long id,
                                  @RequestHeader(name = "X-Sharer-User-Id") long userId) {
        return service.update(itemDto, id, userId);
    }

    @GetMapping("/{itemId}")
    public ItemDtoResponse findById(@PathVariable(name = "itemId") long id,
                                    @RequestHeader(name = "X-Sharer-User-Id") long userId) {
        return service.findById(id, userId);
    }

    @GetMapping()
    public List<ItemDtoResponse> findAll(@RequestHeader(name = "X-Sharer-User-Id") long userId) {
        return service.findAll(userId);
    }

    @GetMapping("/search")
    public List<ItemDtoResponse> search(@RequestHeader(name = "X-Sharer-User-Id") long userId, @RequestParam String text) {
        return service.search(userId, text);
    }
}
