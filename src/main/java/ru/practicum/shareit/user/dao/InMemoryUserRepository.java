package ru.practicum.shareit.user.dao;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.EmailDuplicationException;
import ru.practicum.shareit.exception.IncorrectIdException;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Repository
public class InMemoryUserRepository implements UserRepository {
    private final Map<Long, User> users = new HashMap<>();
    private long count;

    @Override
    public User save(User user) {
        isEmailExist(user);
        user.setId(++count);
        users.put(count, user);
        return user;
    }

    @Override
    public User update(User user, long id) {
        User updatedUser = Optional.ofNullable(users.get(id)).orElseThrow(IncorrectIdException::new);
        if (user.getName() != null && !user.getName().isBlank()) {
            updatedUser.setName(user.getName());
        }
        if (user.getEmail() != null && !user.getEmail().isBlank()) {
            isEmailExist(user);
            updatedUser.setEmail(user.getEmail());
        }
        return updatedUser;
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public Optional<User> findById(long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public void delete(long id) {
        users.remove(id);
    }

    private void isEmailExist(User user) {
        Long id = user.getId();
        String email = user.getEmail();
        if (users.values().stream().anyMatch(u -> u.getEmail().equals(email) && !u.getId().equals(id))) {
            throw new EmailDuplicationException();
        }
    }
}
