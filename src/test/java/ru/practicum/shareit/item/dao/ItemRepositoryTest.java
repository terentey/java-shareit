package ru.practicum.shareit.item.dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.practicum.shareit.util.ItemTest.getNewItem;
import static ru.practicum.shareit.util.RequestTest.getNewRequest;
import static ru.practicum.shareit.util.UserTest.getNewUser;

@DataJpaTest
class ItemRepositoryTest {
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private ItemRepository itemRepo;
    @Autowired
    private ItemRequestRepository itemRequestRepo;

    private User user;
    private Item item;
    private ItemRequest itemRequest;

    @BeforeEach
    void beforeEach() {
        user = userRepo.save(getNewUser("name", "email@mail.com"));

        itemRequest = itemRequestRepo.save(getNewRequest("description", LocalDateTime.now(), user));

        item = itemRepo.save(getNewItem(null, "name", "description", true, user, itemRequest));
    }

    @Test
    void findItemsByUserId() {
        List<Item> result = itemRepo.findItemsByUserId(user.getId());

        assertEquals(item, result.get(0));
    }

    @Test
    void findItemsByUserIdPageable() {
        List<Item> result = itemRepo.findItemsByUserId(user.getId(), PageRequest.of(0, 1));

        assertEquals(item, result.get(0));
    }

    @Test
    void findItemsByAvailableTrueAndDescriptionContainingIgnoreCaseOrNameContainingIgnoreCase_whenName() {
        String text = "nam";

        List<Item> result = itemRepo
                .findItemsByAvailableTrueAndDescriptionContainingIgnoreCaseOrNameContainingIgnoreCase(text, text);

        assertEquals(item, result.get(0));
    }

    @Test
    void findItemsByAvailableTrueAndDescriptionContainingIgnoreCaseOrNameContainingIgnoreCase_whenDescription() {
        String text = "des";

        List<Item> result = itemRepo
                .findItemsByAvailableTrueAndDescriptionContainingIgnoreCaseOrNameContainingIgnoreCase(text, text);

        assertEquals(item, result.get(0));
    }

    @Test
    void findItemsByAvailableTrueAndDescriptionContainingIgnoreCaseOrNameContainingIgnoreCasePageable_whenName() {
        String text = "nam";

        List<Item> result = itemRepo
                .findItemsByAvailableTrueAndDescriptionContainingIgnoreCaseOrNameContainingIgnoreCase(text, text,
                        PageRequest.of(0, 1));

        assertEquals(item, result.get(0));
    }

    @Test
    void findItemsByAvailableTrueAndDescriptionContainingIgnoreCaseOrNameContainingIgnoreCasePageable_whenDescription() {
        String text = "des";

        List<Item> result = itemRepo
                .findItemsByAvailableTrueAndDescriptionContainingIgnoreCaseOrNameContainingIgnoreCase(text, text,
                        PageRequest.of(0, 1));

        assertEquals(item, result.get(0));
    }

    @Test
    void findAllByItemRequestIn() {
        List<Item> result = itemRepo.findAllByItemRequestIn(List.of(itemRequest));

        assertEquals(item, result.get(0));
    }

    @AfterEach
    void afterEach() {
        userRepo.deleteAll();
        itemRepo.deleteAll();
    }
}