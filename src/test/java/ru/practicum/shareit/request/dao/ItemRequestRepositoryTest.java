package ru.practicum.shareit.request.dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static ru.practicum.shareit.util.RequestTest.getNewRequest;
import static ru.practicum.shareit.util.UserTest.getNewUser;

@DataJpaTest
class ItemRequestRepositoryTest {
    private static final LocalDateTime NOW = LocalDateTime.now();


    @Autowired
    private ItemRequestRepository requestRepo;
    @Autowired
    private UserRepository userRepo;

    @BeforeEach
    void beforeEach() {
        User user = getNewUser("name", "email@mail.com");
        userRepo.save(user);

        ItemRequest request = getNewRequest("description", NOW, user);
        requestRepo.save(request);
    }

    @Test
    void findAllByUserIdNotIn() {
        User newUser = getNewUser("new_name", "new_email@mail.com");
        userRepo.save(newUser);

        List<ItemRequest> result = requestRepo.findAllByUserIdNotIn(newUser.getId());

        assertFalse(result.isEmpty());
    }

    @AfterEach
    void afterEach() {
        userRepo.deleteAll();
        requestRepo.deleteAll();
    }
}