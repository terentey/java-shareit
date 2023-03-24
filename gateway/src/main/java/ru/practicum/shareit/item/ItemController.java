package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.util.Marker;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Validated
public class ItemController {
    private final ItemClient client;

    @PostMapping()
    public ResponseEntity<Object> save(@RequestBody @Validated(Marker.OnCreate.class) ItemDto itemDto,
                                       @RequestHeader(name = "X-Sharer-User-Id") long userId) {
        return client.save(itemDto, userId);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> saveComment(@RequestBody @Validated() CommentDto commentDto,
                                              @PathVariable long itemId,
                                              @RequestHeader(name = "X-Sharer-User-Id") long userId) {
        return client.saveComment(commentDto, itemId, userId);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(@RequestBody ItemDto itemDto,
                                         @PathVariable(name = "itemId") long id,
                                         @RequestHeader(name = "X-Sharer-User-Id") long userId) {
        return client.update(itemDto, id, userId);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> findById(@PathVariable(name = "itemId") long id,
                                           @RequestHeader(name = "X-Sharer-User-Id") long userId) {
        return client.findById(id, userId);
    }

    @GetMapping()
    public ResponseEntity<Object> findAll(@RequestHeader(name = "X-Sharer-User-Id") long userId,
                                          @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                          @RequestParam(defaultValue = "20") @Positive int size) {
        return client.findAll(userId, from, size);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> search(@RequestHeader(name = "X-Sharer-User-Id") long userId,
                                         @RequestParam String text,
                                         @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                         @RequestParam(defaultValue = "20") @Positive int size) {
        return client.search(userId, text, from, size);
    }
}
