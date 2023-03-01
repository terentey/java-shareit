package ru.practicum.shareit.item.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemMapper {
    public static ItemDto mapToItemDto(Item item) {
        ItemDto itemDto = ItemDto
                .builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .build();
        if (item.getComments() != null) {
            itemDto.setComments(CommentMapper.mapToCommentDto(item.getComments()));
        }
        return itemDto;
    }

    public static Item mapToItem(ItemDto itemDto, User user) {
        Item item = new Item();
        item.setId(itemDto.getId());
        item.setUser(user);
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());
        return item;
    }

    public static List<ItemDto> mapToItemDto(List<Item> items) {
        return items
                .stream()
                .map(ItemMapper::mapToItemDtoByOwner)
                .sorted(Comparator.comparing(ItemDto::getId))
                .collect(Collectors.toList());
    }

    public static ItemDto mapToItemDtoByOwner(Item item) {
        ItemDto itemDto = mapToItemDto(item);
        if (!item.getBookings().isEmpty() && item.getBookings().size() != 1) {
            setBookingDto(itemDto, item);
        }
        return itemDto;
    }

    private static void setBookingDto(ItemDto itemDto, Item item) {
        itemDto.setLastBooking(BookingMapper.mapToBookingDtoForItemDto(item.getBookings().stream()
                .filter(b -> b.getEnd().isBefore(LocalDateTime.now()))
                .min(Comparator.comparing(Booking::getEnd)).get()));
        itemDto.setNextBooking(BookingMapper.mapToBookingDtoForItemDto(item.getBookings().stream()
                .filter(b -> b.getStart().isAfter(LocalDateTime.now()))
                .min(Comparator.comparing(Booking::getStart)).get()));
    }
}
