package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService service;

    @PostMapping()
    public BookingDtoResponse save(@RequestBody BookingDtoRequest bookingDto,
                                   @RequestHeader(name = "X-Sharer-User-Id") long userId) {
        return service.save(bookingDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDtoResponse update(@PathVariable(name = "bookingId") long id,
                                     @RequestHeader(name = "X-Sharer-User-Id") long userId,
                                     @RequestParam boolean approved) {
        return service.update(id, userId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDtoResponse findById(@PathVariable(name = "bookingId") long id,
                                       @RequestHeader(name = "X-Sharer-User-Id") long ownerId) {
        return service.findById(id, ownerId);
    }

    @GetMapping
    public List<BookingDtoResponse> findByUserId(@RequestHeader(name = "X-Sharer-User-Id") long userId,
                                                 @RequestParam() String state,
                                                 @RequestParam() int from,
                                                 @RequestParam() int size) {
        return service.findAllByUserId(userId, state, from, size);
    }

    @GetMapping("/owner")
    public List<BookingDtoResponse> findByOwnerId(@RequestHeader(name = "X-Sharer-User-Id") long ownerId,
                                                  @RequestParam() String state,
                                                  @RequestParam() int from,
                                                  @RequestParam() int size) {
        return service.findAllByOwnerId(ownerId, state, from, size);
    }
}
