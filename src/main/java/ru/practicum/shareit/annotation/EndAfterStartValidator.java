package ru.practicum.shareit.annotation;

import ru.practicum.shareit.booking.dto.BookingDtoRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EndAfterStartValidator implements ConstraintValidator<EndAfterStart, BookingDtoRequest> {

    @Override
    public boolean isValid(BookingDtoRequest bookingDtoRequest, ConstraintValidatorContext constraintValidatorContext) {
        return bookingDtoRequest.getEnd().isAfter(bookingDtoRequest.getStart());
    }
}
