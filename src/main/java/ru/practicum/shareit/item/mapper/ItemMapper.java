package ru.practicum.shareit.item.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemDtoRequest;
import ru.practicum.shareit.item.dto.ItemDtoResponse;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemMapper {
    public static ItemDtoResponse mapToItemDto(Item item) {
        ItemDtoResponse itemDto = ItemDtoResponse
                .builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .build();
        if (item.getItemRequest() != null) {
            itemDto.setRequestId(item.getItemRequest().getId());
        }
        return itemDto;
    }

    public static ItemDtoResponse mapToItemDto(Item item, List<Comment> comments) {
        ItemDtoResponse itemDto = mapToItemDto(item);
        if (comments != null && !comments.isEmpty()) {
            itemDto.setComments(CommentMapper.mapToCommentDto(comments));
        } else {
            itemDto.setComments(Collections.emptyList());
        }
        return itemDto;
    }

    public static ItemDtoResponse mapToItemDto(Item item, List<Comment> comments, List<Booking> bookings) {
        ItemDtoResponse itemDto = mapToItemDto(item, comments);
        if (bookings != null && !bookings.isEmpty()) {
            itemDto.setLastBooking(mapToBookingDto(bookings.stream()
                    .filter(b -> !b.getStart().isAfter(LocalDateTime.now()))
                    .findFirst().orElse(null)));
            itemDto.setNextBooking(mapToBookingDto(bookings.stream()
                    .filter(b -> b.getStart().isAfter(LocalDateTime.now()))
                    .reduce((f, s) -> s).orElse(null)));
        }
        return itemDto;
    }

    public static List<ItemDtoResponse> mapToItemDto(List<Item> items,
                                                     Map<Item, List<Comment>> comments,
                                                     Map<Item, List<Booking>> bookings) {
        return items
                .stream()
                .map(i -> mapToItemDto(i, comments.get(i), bookings.get(i)))
                .sorted(Comparator.comparing(ItemDtoResponse::getId))
                .collect(Collectors.toList());
    }

    public static Item mapToItem(ItemDtoRequest itemDto, User user) {
        Item item = new Item();
        item.setId(itemDto.getId());
        item.setUser(user);
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());
        return item;
    }

    private static ItemDtoResponse.BookingDto mapToBookingDto(Booking booking) {
        if (booking == null) {
            return null;
        } else {
            return new ItemDtoResponse
                    .BookingDto(booking.getId(), booking.getStart(), booking.getEnd(), booking.getUser().getId());
        }
    }
}
