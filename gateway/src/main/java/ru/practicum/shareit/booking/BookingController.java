package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Validated
public class BookingController {
    private final BookingClient client;

    @PostMapping()
    public ResponseEntity<Object> save(@RequestBody @Validated BookingDto bookingDto,
                                       @RequestHeader(name = "X-Sharer-User-Id") long userId) {
        return client.save(bookingDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> update(@PathVariable(name = "bookingId") long id,
                                         @RequestHeader(name = "X-Sharer-User-Id") long userId,
                                         @RequestParam boolean approved) {
        return client.update(id, userId, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> findById(@PathVariable(name = "bookingId") long id,
                                           @RequestHeader(name = "X-Sharer-User-Id") long ownerId) {
        return client.findById(id, ownerId);
    }

    @GetMapping
    public ResponseEntity<Object> findByUserId(@RequestHeader(name = "X-Sharer-User-Id") long userId,
                                               @RequestParam(defaultValue = "ALL") String state,
                                               @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                               @RequestParam(defaultValue = "20") @Positive int size) {
        return client.findAllByUserId(userId, state, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> findByOwnerId(@RequestHeader(name = "X-Sharer-User-Id") long ownerId,
                                                @RequestParam(defaultValue = "ALL") String state,
                                                @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                @RequestParam(defaultValue = "20") @Positive int size) {
        return client.findAllByOwnerId(ownerId, state, from, size);
    }
}
