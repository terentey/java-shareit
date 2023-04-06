package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    private static final Sort SORT_BY_START_DESC = Sort.by(Sort.Direction.DESC, "start");

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

    @Transactional(readOnly = true)
    @Override
    public BookingDtoResponse findById(long id, long userId) {
        return BookingMapper.mapToBookingDto(repository.findByIdAndUserIdOrOwnerId(id, userId).orElseThrow(IncorrectIdException::new));
    }

    @Transactional(readOnly = true)
    @Override
    public List<BookingDtoResponse> findAllByUserId(long userId, String status, int from, int size) {
        final State state = getState(status);
        userRepo.findById(userId).orElseThrow(IncorrectIdException::new);
        int pageNum = from / size;
        Page<Booking> bookings;
        switch (state) {
            case ALL:
                bookings = repository.findAllByUserId(userId, PageRequest.of(pageNum, size, SORT_BY_START_DESC));
                break;
            case CURRENT:
                bookings = repository.findAllByUserIdAndCurrentTime(userId,
                        PageRequest.of(pageNum, size, SORT_BY_START_DESC));
                break;
            case PAST:
                bookings = repository.findAllByUserIdAndEndBefore(userId,
                        PageRequest.of(pageNum, size, SORT_BY_START_DESC));
                break;
            case FUTURE:
                bookings = repository.findAllByUserIdAndStartAfter(userId,
                        PageRequest.of(pageNum, size, SORT_BY_START_DESC));
                break;
            case WAITING:
                bookings = repository.findAllByUserIdAndStatus(userId, Status.WAITING,
                        PageRequest.of(pageNum, size, SORT_BY_START_DESC));
                break;
            case REJECTED:
                bookings = repository.findAllByUserIdAndStatus(userId, Status.REJECTED,
                        PageRequest.of(pageNum, size, SORT_BY_START_DESC));
                break;
            default:
                throw new IncorrectState();
        }
        return BookingMapper.mapToBookingDto(bookings.getContent());
    }

    @Transactional(readOnly = true)
    @Override
    public List<BookingDtoResponse> findAllByOwnerId(long ownerId, String status, int from, int size) {
        final State state = getState(status);
        userRepo.findById(ownerId).orElseThrow(IncorrectIdException::new);
        int pageNum = from / size;
        Page<Booking> bookings;
        switch (state) {
            case ALL:
                bookings = repository.findAllByOwnerId(ownerId, PageRequest.of(pageNum, size, SORT_BY_START_DESC));
                break;
            case CURRENT:
                bookings = repository.findAllByOwnerIdAndCurrentTime(ownerId,
                        PageRequest.of(pageNum, size, SORT_BY_START_DESC));
                break;
            case PAST:
                bookings = repository.findAllByOwnerIdAndEndBefore(ownerId,
                        PageRequest.of(pageNum, size, SORT_BY_START_DESC));
                break;
            case FUTURE:
                bookings = repository.findAllByOwnerIdAndStartAfter(ownerId,
                        PageRequest.of(pageNum, size, SORT_BY_START_DESC));
                break;
            case WAITING:
                bookings = repository.findAllByOwnerIdAndStatus(ownerId, Status.WAITING,
                        PageRequest.of(pageNum, size, SORT_BY_START_DESC));
                break;
            case REJECTED:
                bookings = repository.findAllByOwnerIdAndStatus(ownerId, Status.REJECTED,
                        PageRequest.of(pageNum, size, SORT_BY_START_DESC));
                break;
            default:
                throw new IncorrectState();
        }
        return BookingMapper.mapToBookingDto(bookings.getContent());
    }

    private State getState(String state) {
        try {
            return State.valueOf(state);
        } catch (Throwable e) {
            throw new IncorrectState();
        }
    }
}
