package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class BookingMapperTest {

    private final BookingMapper bookingMapper = Mappers.getMapper(BookingMapper.class);

    @Test
    void toBookingResponseDto_ShouldMapCorrectly() {

        User booker = new User();
        booker.setId(1L);
        booker.setName("Booker");

        Item item = new Item();
        item.setId(1L);
        item.setName("Test Item");

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setStart(LocalDateTime.now());
        booking.setEnd(LocalDateTime.now().plusDays(1));
        booking.setStatus(BookingStatus.WAITING);
        booking.setBooker(booker);
        booking.setItem(item);

        BookingResponseDto dto = bookingMapper.toBookingResponseDto(booking);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals(BookingStatus.WAITING, dto.getStatus());
        assertNotNull(dto.getItem());
        assertEquals(1L, dto.getItem().getId());
        assertNotNull(dto.getBooker());
        assertEquals(1L, dto.getBooker().getId());
    }

    @Test
    void toBooking_ShouldMapCorrectly() {

        BookingRequestDto requestDto = new BookingRequestDto();
        requestDto.setItemId(1L);
        requestDto.setStart(LocalDateTime.now());
        requestDto.setEnd(LocalDateTime.now().plusDays(1));

        Booking booking = bookingMapper.toBooking(requestDto, 1L);

        assertNotNull(booking);
        assertNull(booking.getId()); // ID должен игнорироваться
        assertEquals(BookingStatus.WAITING, booking.getStatus());
        assertNotNull(booking.getItem());
        assertEquals(1L, booking.getItem().getId());
        assertNotNull(booking.getBooker());
        assertEquals(1L, booking.getBooker().getId());
    }

    @Test
    void toBooking_WithNullRequest_ShouldReturnNull() {

        Booking booking = bookingMapper.toBooking(null, 1L);

        assertNull(booking);
    }
}