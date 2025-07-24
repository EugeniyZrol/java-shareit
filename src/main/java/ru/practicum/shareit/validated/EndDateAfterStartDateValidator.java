package ru.practicum.shareit.validated;

import ru.practicum.shareit.booking.dto.BookingDto;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EndDateAfterStartDateValidator
        implements ConstraintValidator<EndDateAfterStartDate, BookingDto> {

    @Override
    public boolean isValid(BookingDto dto, ConstraintValidatorContext context) {
        if (dto.getStart() == null || dto.getEnd() == null) {
            return true;
        }
        return dto.getEnd().isAfter(dto.getStart());
    }
}