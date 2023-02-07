package ru.practicum.shareit.user.dao;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.EmailDuplicationException;
import ru.practicum.shareit.exception.IncorrectIdException;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Repository
public class InMemoryUserRepository implements UserRepository {
    private final static Map<Long, User> USERS = new HashMap<>();
    private long count;

    @Override
    public User save(User user) {
        isEmailExist(user.getEmail());
        user.setId(++count);
        USERS.put(count, user);
        return user;
    }

    @Override
    public User update(Map<String, String> patch, long id) {
        User user = Optional.ofNullable(USERS.get(id)).orElseThrow(IncorrectIdException::new);
        if (patch.containsKey("name")) user.setName(patch.get("name"));
        if (patch.containsKey("email")) {
            String email = patch.get("email");
            isEmailExist(email);
            user.setEmail(email);
        }
        USERS.put(id, user);
        return user;
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(USERS.values());
    }

    @Override
    public Optional<User> findById(long id) {
        return Optional.ofNullable(USERS.get(id));
    }

    @Override
    public void delete(long id) {
        USERS.remove(id);
    }

    private void isEmailExist(String email) {
        if (USERS.values().stream().anyMatch(u -> u.getEmail().equals(email))) throw new EmailDuplicationException();
    }
}
