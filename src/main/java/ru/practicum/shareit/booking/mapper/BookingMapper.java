package ru.practicum.shareit.booking.mapper;

import org.mapstruct.Mapper;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.dto.BookingDto;

@Mapper(componentModel = "spring")
public interface BookingMapper {
    BookingDto toItemDto(Booking booking);
}
