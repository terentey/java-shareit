package ru.practicum.shareit.util;

import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.Instant;

public class CommentTest {
    public static Comment getNewComment(Long id, String text, Instant created, Item item, User user) {
        Comment comment = getNewComment(text, created, item, user);
        comment.setId(id);
        return comment;
    }

    public static Comment getNewComment(String text, Instant created, Item item, User user) {
        Comment comment = new Comment();
        comment.setText(text);
        comment.setCreated(created);
        comment.setItem(item);
        comment.setUser(user);
        return comment;
    }
}
