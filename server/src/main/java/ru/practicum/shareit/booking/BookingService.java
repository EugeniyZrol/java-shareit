package ru.practicum.shareit.booking;

import java.util.List;

public interface BookingService {
    BookingResponseDto createBooking(BookingRequestDto bookingRequestDto, Long userId);

    BookingResponseDto approveBooking(Long bookingId, Boolean approved, Long userId);

    BookingResponseDto getBookingById(Long bookingId, Long userId);

    List<BookingResponseDto> getUserBookings(String state, Long userId, Integer from, Integer size);

    List<BookingResponseDto> getOwnerBookings(String state, Long userId, Integer from, Integer size);
}