package ru.practicum.shareit.booking.dao;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("select b " +
            "from Booking b " +
            "where b.item.id=?1 and b.user.id=?2 and b.status='APPROVED' and b.start < now()")
    List<Booking> findByItemIdAndUserIdAndStatusApprovedAndStartBeforeNow(long itemId, long userId, Sort sort);

    @Query("select b " +
            "from Booking b " +
            "where b.id=?1 and (b.user.id=?2 or b.item.user.id=?2)")
    Optional<Booking> findByIdAndUserIdOrOwnerId(long id, long userId);

    @Query("select b " +
            "from Booking b " +
            "where b.item in (?1) and b.status not in (?2)")
    List<Booking> findByItemInAndStatusNotIn(List<Item> items, Status status, Sort sort);

    @Query("select b " +
            "from Booking b " +
            "where b.user.id=?1")
    List<Booking> findAllByUserId(long userId, Sort sort);

    @Query("select b " +
            "from Booking b " +
            "where b.user.id=?1 and b.status=?2")
    List<Booking> findAllByUserIdAndStatus(long userId, Status status, Sort sort);

    @Query("select b " +
            "from Booking b " +
            "where b.user.id=?1 and now() between b.start and b.end")
    List<Booking> findAllByUserIdAndStatusAndCurrentTime(long userId, Sort sort);

    @Query("select b " +
            "from Booking b " +
            "where b.user.id=?1 and b.start > now()")
    List<Booking> findAllByUserIdAndStatusAndStartAfter(long userId, Sort sort);

    @Query("select b " +
            "from Booking b " +
            "where b.user.id=?1 and b.end < now()")
    List<Booking> findAllByUserIdAndStatusAndEndBefore(long userId, Sort sort);

    @Query("select b " +
            "from Booking b " +
            "where b.item.user.id=?1")
    List<Booking> findAllByOwnerId(long ownerId, Sort sort);

    @Query("select b " +
            "from Booking b " +
            "where b.item.user.id=?1 and b.status not in ?2")
    List<Booking> findAllByOwnerIdAndStatusNotIn(long ownerId, Status status, Sort sort);

    @Query("select b " +
            "from Booking b " +
            "where b.item.user.id=?1 and b.status=?2")
    List<Booking> findAllByOwnerIdAndStatus(long ownerId, Status status, Sort sort);

    @Query("select b " +
            "from Booking b " +
            "where b.item.user.id=?1 and now() between b.start and b.end")
    List<Booking> findAllByOwnerIdAndStatusAndCurrentTime(long ownerId, Sort sort);

    @Query("select b " +
            "from Booking b " +
            "where b.item.user.id=?1 and b.start > now()")
    List<Booking> findAllByOwnerIdAndStatusAndStartAfter(long ownerId, Sort sort);

    @Query("select b " +
            "from Booking b " +
            "where b.item.user.id=?1 and b.end < now()")
    List<Booking> findAllByOwnerIdAndStatusAndEndBefore(long ownerId, Sort sort);
}
