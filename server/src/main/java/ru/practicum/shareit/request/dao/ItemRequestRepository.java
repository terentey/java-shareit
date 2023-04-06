package ru.practicum.shareit.request.dao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {
    List<ItemRequest> findAllByUserOrderByCreated(User user);

    @Query("select r " +
            "from ItemRequest r " +
            "where r.user.id not in(?1)")
    List<ItemRequest> findAllByUserIdNotIn(long userId);

    @Query("select r " +
            "from ItemRequest r " +
            "where r.user.id not in(?1)")
    List<ItemRequest> findAllByUserIdNotIn(long userId, Pageable pageable);
}
