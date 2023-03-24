package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDtoRequest;
import ru.practicum.shareit.item.dto.CommentDtoResponse;
import ru.practicum.shareit.item.dto.ItemDtoRequest;
import ru.practicum.shareit.item.dto.ItemDtoResponse;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService service;

    @PostMapping()
    public ItemDtoResponse save(@RequestBody ItemDtoRequest itemDto,
                                @RequestHeader(name = "X-Sharer-User-Id") long userId) {
        return service.save(itemDto, userId);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDtoResponse saveComment(@RequestBody CommentDtoRequest commentDto,
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
    public List<ItemDtoResponse> findAll(@RequestHeader(name = "X-Sharer-User-Id") long userId,
                                         @RequestParam() int from,
                                         @RequestParam() int size) {
        return service.findAll(userId, from, size);
    }

    @GetMapping("/search")
    public List<ItemDtoResponse> search(@RequestHeader(name = "X-Sharer-User-Id") long userId,
                                        @RequestParam String text,
                                        @RequestParam() int from,
                                        @RequestParam() int size) {
        return service.search(userId, text, from, size);
    }
}
