package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDtoRequest;
import ru.practicum.shareit.request.dto.ItemRequestDtoResponse;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Validated
public class ItemRequestController {
    private final ItemRequestService service;

    @PostMapping
    public ItemRequestDtoResponse save(@RequestBody @Validated ItemRequestDtoRequest itemRequestDto,
                                       @RequestHeader(name = "X-Sharer-User-Id") long userId) {
        return service.save(itemRequestDto, userId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDtoResponse findById(@PathVariable(name = "requestId") long id,
                                           @RequestHeader(name = "X-Sharer-User-Id") long userId) {
        return service.findById(id, userId);
    }

    @GetMapping()
    public List<ItemRequestDtoResponse> findAll(@RequestHeader(name = "X-Sharer-User-Id") long userId) {
        return service.findAllByOwner(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDtoResponse> findAll(@RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                @RequestParam(required = false) @Positive Integer size,
                                                @RequestHeader(name = "X-Sharer-User-Id") long userId) {
        return service.findAll(from, size, userId);
    }
}
