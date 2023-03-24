package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Validated
public class ItemRequestController {
    private final ItemRequestClient client;

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Validated ItemRequestDto itemRequestDto,
                                       @RequestHeader(name = "X-Sharer-User-Id") long userId) {
        return client.save(itemRequestDto, userId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> findById(@PathVariable(name = "requestId") long id,
                                           @RequestHeader(name = "X-Sharer-User-Id") long userId) {
        return client.findById(id, userId);
    }

    @GetMapping()
    public ResponseEntity<Object> findAll(@RequestHeader(name = "X-Sharer-User-Id") long userId) {
        return client.findAllByOwner(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> findAll(@RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                          @RequestParam(defaultValue = "20") @Positive int size,
                                          @RequestHeader(name = "X-Sharer-User-Id") long userId) {
        return client.findAll(from, size, userId);
    }
}
