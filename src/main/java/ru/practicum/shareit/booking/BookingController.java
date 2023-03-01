package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.util.Marker;

import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService service;

    @PostMapping()
    public BookingDto save(@RequestBody @Validated(Marker.OnCreate.class) BookingDto bookingDto,
                           @RequestHeader(name = "X-Sharer-User-Id") long userId) {
        return service.save(bookingDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto update(@PathVariable(name = "bookingId") long id,
                             @RequestHeader(name = "X-Sharer-User-Id") long userId,
                             @RequestParam boolean approved) {
        return service.update(id, userId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDto findById(@PathVariable(name = "bookingId") long id,
                               @RequestHeader(name = "X-Sharer-User-Id") long ownerId) {
        return service.findById(id, ownerId);
    }

    @GetMapping
    public List<BookingDto> findByUserId(@RequestHeader(name = "X-Sharer-User-Id") long userId,
                                         @RequestParam(defaultValue = "ALL") String state) {
        return service.findAllByUserId(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingDto> findByOwnerId(@RequestHeader(name = "X-Sharer-User-Id") long ownerId,
                                          @RequestParam(defaultValue = "ALL") String state) {
        return service.findAllByOwnerId(ownerId, state);
    }
}
