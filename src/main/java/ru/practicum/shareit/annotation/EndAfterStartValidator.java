package ru.practicum.shareit.annotation;

import ru.practicum.shareit.booking.dto.BookingDtoRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class EndAfterStartValidator implements ConstraintValidator<EndAfterStart, BookingDtoRequest> {

    @Override
    public boolean isValid(BookingDtoRequest bookingDtoRequest, ConstraintValidatorContext constraintValidatorContext) {
        LocalDateTime start = bookingDtoRequest.getStart();
        LocalDateTime end = bookingDtoRequest.getEnd();
        if (start == null || end == null) {
            return false;
        }
        return bookingDtoRequest.getEnd().isAfter(bookingDtoRequest.getStart());
    }
}
