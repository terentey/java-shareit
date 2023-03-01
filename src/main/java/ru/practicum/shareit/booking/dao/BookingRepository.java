package ru.practicum.shareit.booking.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("select b " +
            "from Booking b " +
            "where b.item.user.id=?1 and b.end < now() " +
            "order by b.start desc")
    List<Booking> findAllByOwnerIdAndEndBefore(long ownerId);

    @Query("select b " +
            "from Booking b " +
            "where b.id=?1 and (b.user.id=?2 or b.item.user.id=?2)")
    Optional<Booking> findByIdAndUserIdOrOwnerId(long id, long userId);

    @Query("select b " +
            "from Booking b " +
            "where b.user.id=?1 " +
            "order by b.start desc")
    List<Booking> findAllByUserId(long userId);

    @Query("select b " +
            "from Booking b " +
            "where b.user.id=?1 and b.status=?2 " +
            "order by b.start desc")
    List<Booking> findAllByUserIdAndStatus(long userId, Status status);

    @Query("select b " +
            "from Booking b " +
            "where b.user.id=?1 and now() between b.start and b.end " +
            "order by b.start desc")
    List<Booking> findAllByUserIdAndStatusAndCurrentTime(long userId);

    @Query("select b " +
            "from Booking b " +
            "where b.user.id=?1 and b.start > now() " +
            "order by b.start desc")
    List<Booking> findAllByUserIdAndStatusAndStartAfter(long userId);

    @Query("select b " +
            "from Booking b " +
            "where b.user.id=?1 and b.end < now() " +
            "order by b.start desc")
    List<Booking> findAllByUserIdAndStatusAndEndAfter(long userId);

    @Query("select b " +
            "from Booking b " +
            "where b.item.user.id=?1 " +
            "order by b.start desc")
    List<Booking> findAllByOwnerId(long ownerId);

    @Query("select b " +
            "from Booking b " +
            "where b.item.user.id=?1 and b.status=?2 " +
            "order by b.start desc")
    List<Booking> findAllByOwnerIdAndStatus(long ownerId, Status status);

    @Query("select b " +
            "from Booking b " +
            "where b.item.user.id=?1 and now() between b.start and b.end " +
            "order by b.start desc")
    List<Booking> findAllByOwnerIdAndStatusAndCurrentTime(long ownerId);

    @Query("select b " +
            "from Booking b " +
            "where b.item.user.id=?1 and b.start > now() " +
            "order by b.start desc")
    List<Booking> findAllByOwnerIdAndStatusAndStartAfter(long ownerId);

    @Query("select b " +
            "from Booking b " +
            "where b.item.user.id=?1 and b.end < now() " +
            "order by b.start desc")
    List<Booking> findAllByOwnerIdAndStatusAndEndAfter(long ownerId);
}
