package ru.practicum.shareit.item.dao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findItemsByUserId(long userId);

    List<Item> findItemsByUserId(long userId, Pageable pageable);

    List<Item> findItemsByAvailableTrueAndDescriptionContainingIgnoreCaseOrNameContainingIgnoreCase(String text1, String text2);

    List<Item> findItemsByAvailableTrueAndDescriptionContainingIgnoreCaseOrNameContainingIgnoreCase(String text1, String text2, Pageable pageable);

    List<Item> findAllByItemRequestIn(List<ItemRequest> itemRequest);
}
