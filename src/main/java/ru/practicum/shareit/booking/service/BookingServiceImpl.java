package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
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

import java.util.List;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final static Sort SORT_BY_START_DESC = Sort.by(Sort.Direction.DESC, "start");

    private final BookingRepository repository;
    private final ItemRepository itemRepo;
    private final UserRepository userRepo;

    @Transactional
    @Override
    public BookingDtoResponse save(BookingDtoRequest bookingDto, long userId) {
        Item item = itemRepo.findById(bookingDto.getItemId()).orElseThrow(IncorrectIdException::new);
        User booker = userRepo.findById(userId).orElseThrow(IncorrectIdException::new);
        if (booker.getId() != userId || item.getUser().getId() == userId) {
            throw new IncorrectIdException();
        } else if (item.getAvailable()) {
            return BookingMapper
                    .mapToBookingDto(repository
                            .saveAndFlush(BookingMapper.mapToBooking(bookingDto, Status.WAITING, item, booker)));
        } else {
            throw new UnavailableItemException();
        }
    }

    @Transactional
    @Override
    public BookingDtoResponse update(long id, long ownerId, boolean approved) {
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
        return BookingMapper.mapToBookingDto(booking);
    }

    @Transactional
    @Override
    public BookingDtoResponse findById(long id, long userId) {
        return BookingMapper.mapToBookingDto(repository.findByIdAndUserIdOrOwnerId(id, userId).orElseThrow(IncorrectIdException::new));
    }

    @Transactional
    @Override
    public List<BookingDtoResponse> findAllByUserId(long userId, String status) {
        final State state = getState(status);
        userRepo.findById(userId).orElseThrow(IncorrectIdException::new);
        final List<Booking> bookings;
        switch (state) {
            case ALL:
                bookings = repository.findAllByUserId(userId, SORT_BY_START_DESC);
                break;
            case CURRENT:
                bookings = repository.findAllByUserIdAndStatusAndCurrentTime(userId, SORT_BY_START_DESC);
                break;
            case PAST:
                bookings = repository.findAllByUserIdAndStatusAndEndBefore(userId, SORT_BY_START_DESC);
                break;
            case FUTURE:
                bookings = repository.findAllByUserIdAndStatusAndStartAfter(userId, SORT_BY_START_DESC);
                break;
            case WAITING:
                bookings = repository.findAllByUserIdAndStatus(userId, Status.WAITING, SORT_BY_START_DESC);
                break;
            case REJECTED:
                bookings = repository.findAllByUserIdAndStatus(userId, Status.REJECTED, SORT_BY_START_DESC);
                break;
            default:
                throw new IncorrectState();
        }
        return BookingMapper.mapToBookingDto(bookings);
    }

    @Transactional
    @Override
    public List<BookingDtoResponse> findAllByOwnerId(long ownerId, String status) {
        final State state = getState(status);
        userRepo.findById(ownerId).orElseThrow(IncorrectIdException::new);
        List<Booking> bookings;
        switch (state) {
            case ALL:
                bookings = repository.findAllByOwnerId(ownerId, SORT_BY_START_DESC);
                break;
            case CURRENT:
                bookings = repository.findAllByOwnerIdAndStatusAndCurrentTime(ownerId, SORT_BY_START_DESC);
                break;
            case PAST:
                bookings = repository.findAllByOwnerIdAndStatusAndEndBefore(ownerId, SORT_BY_START_DESC);
                break;
            case FUTURE:
                bookings = repository.findAllByOwnerIdAndStatusAndStartAfter(ownerId, SORT_BY_START_DESC);
                break;
            case WAITING:
                bookings = repository.findAllByOwnerIdAndStatus(ownerId, Status.WAITING, SORT_BY_START_DESC);
                break;
            case REJECTED:
                bookings = repository.findAllByOwnerIdAndStatus(ownerId, Status.REJECTED, SORT_BY_START_DESC);
                break;
            default:
                throw new IncorrectState();
        }
        return BookingMapper.mapToBookingDto(bookings);
    }

    private State getState(String state) {
        try {
            return State.valueOf(state);
        } catch (Throwable e) {
            throw new IncorrectState();
        }
    }
}
