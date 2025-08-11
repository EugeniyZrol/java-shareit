package ru.practicum.shareit.booking;

import ru.practicum.shareit.item.ItemResponse;

import java.time.LocalDateTime;
import java.util.List;

public class BookingInfoContext {
    private final BookingRepository bookingRepository;

    public BookingInfoContext(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public ItemResponse.BookingInfo getLastBooking(Long itemId) {
        List<Booking> lastBookings = bookingRepository.findLastBooking(
                itemId,
                LocalDateTime.now()
        );
        return lastBookings.isEmpty() ? null : toBookingInfo(lastBookings.getFirst());
    }

    public ItemResponse.BookingInfo getNextBooking(Long itemId) {
        List<Booking> nextBookings = bookingRepository.findNextBooking(
                itemId,
                LocalDateTime.now()
        );
        return nextBookings.isEmpty() ? null : toBookingInfo(nextBookings.getFirst());
    }

    private ItemResponse.BookingInfo toBookingInfo(Booking booking) {
        if (booking == null) {
            return null;
        }
        return new ItemResponse.BookingInfo(
                booking.getId(),
                booking.getBooker().getId(),
                booking.getStart(),
                booking.getEnd()
        );
    }
}