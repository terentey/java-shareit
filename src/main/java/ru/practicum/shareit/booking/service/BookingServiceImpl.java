package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exception.IncorrectIdException;
import ru.practicum.shareit.exception.IncorrectState;
import ru.practicum.shareit.exception.UnavailableItemException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository repository;
    private final ItemRepository itemRepo;
    private final UserRepository userRepo;

    @Override
    public BookingDto save(BookingDto bookingDto, long userId) {
        Item item = itemRepo.findById(bookingDto.getItemId()).orElseThrow(IncorrectIdException::new);
        User booker = userRepo.findById(userId).orElseThrow(IncorrectIdException::new);
        LocalDateTime start = LocalDateTime.parse(bookingDto.getStart());
        LocalDateTime end = LocalDateTime.parse(bookingDto.getEnd());
        LocalDateTime now = LocalDateTime.now();
        boolean isEndAfterStart = end.isAfter(start);
        boolean isEndAndStartInFeature = end.isAfter(now) && start.isAfter(now);
        if (booker.getId() != userId || item.getUser().getId() == userId) {
            throw new IncorrectIdException();
        } else if (item.getAvailable() && isEndAfterStart && isEndAndStartInFeature) {
            return BookingMapper
                    .mapToBookingDto(repository
                            .saveAndFlush(BookingMapper.mapToBooking(bookingDto, Status.WAITING, item, booker)));
        } else {
            throw new UnavailableItemException();
        }
    }

    @Override
    public BookingDto update(long id, long ownerId, boolean approved) {
        Booking booking = repository.findById(id).orElseThrow(IncorrectIdException::new);
        if (booking.getItem().getUser().getId() != ownerId) {
            throw new IncorrectIdException();
        } else if (booking.getStatus().equals(Status.APPROVED)) {
            throw new UnavailableItemException();
        } else if (approved) {
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }
        return BookingMapper.mapToBookingDto(repository.saveAndFlush(booking));
    }

    @Override
    public BookingDto findById(long id, long userId) {
        return BookingMapper.mapToBookingDto(repository.findByIdAndUserIdOrOwnerId(id, userId).orElseThrow(IncorrectIdException::new));
    }

    @Override
    public List<BookingDto> findAllByUserId(long userId, String status) {
        final State state = State.fromString(status);
        userRepo.findById(userId).orElseThrow(IncorrectIdException::new);
        final List<Booking> bookings;
        switch (state) {
            case ALL:
                bookings = repository.findAllByUserId(userId);
                break;
            case CURRENT:
                bookings = repository.findAllByUserIdAndStatusAndCurrentTime(userId);
                break;
            case PAST:
                bookings = repository.findAllByUserIdAndStatusAndEndAfter(userId);
                break;
            case FUTURE:
                bookings = repository.findAllByUserIdAndStatusAndStartAfter(userId);
                break;
            case WAITING:
                bookings = repository.findAllByUserIdAndStatus(userId, Status.WAITING);
                break;
            case REJECTED:
                bookings = repository.findAllByUserIdAndStatus(userId, Status.REJECTED);
                break;
            default:
                throw new IncorrectState();
        }
        return BookingMapper.mapToBookingDto(bookings);
    }

    @Override
    public List<BookingDto> findAllByOwnerId(long ownerId, String status) {
        final State state = State.fromString(status);
        userRepo.findById(ownerId).orElseThrow(IncorrectIdException::new);
        List<Booking> bookings;
        switch (state) {
            case ALL:
                bookings = repository.findAllByOwnerId(ownerId);
                break;
            case CURRENT:
                bookings = repository.findAllByOwnerIdAndStatusAndCurrentTime(ownerId);
                break;
            case PAST:
                bookings = repository.findAllByOwnerIdAndStatusAndEndAfter(ownerId);
                break;
            case FUTURE:
                bookings = repository.findAllByOwnerIdAndStatusAndStartAfter(ownerId);
                break;
            case WAITING:
                bookings = repository.findAllByOwnerIdAndStatus(ownerId, Status.WAITING);
                break;
            case REJECTED:
                bookings = repository.findAllByOwnerIdAndStatus(ownerId, Status.REJECTED);
                break;
            default:
                throw new IncorrectState();
        }
        return BookingMapper.mapToBookingDto(bookings);
    }
}
