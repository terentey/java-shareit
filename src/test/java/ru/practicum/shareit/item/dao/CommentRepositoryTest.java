package ru.practicum.shareit.item.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.practicum.shareit.util.CommentTest.getNewComment;
import static ru.practicum.shareit.util.ItemTest.getNewItem;
import static ru.practicum.shareit.util.UserTest.getNewUser;

@DataJpaTest
class CommentRepositoryTest {
    private static final Sort SORT = Sort.by(Sort.Direction.DESC, "created");

    @Autowired
    private UserRepository userRepo;
    @Autowired
    private ItemRepository itemRepo;
    @Autowired
    private CommentRepository commentRepo;

    @Test
    void findByItemIn() {
        User user = userRepo.save(getNewUser("user", "user@mail.com"));
        Item item = itemRepo.save(getNewItem("name", "description", true, user));
        Comment comment = commentRepo.save(getNewComment("text", Instant.now(), item, user));

        List<Comment> result = commentRepo.findByItemIn(List.of(item), SORT);

        assertEquals(comment, result.get(0));
    }
}