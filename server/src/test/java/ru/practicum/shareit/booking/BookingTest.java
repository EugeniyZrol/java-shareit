package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class BookingTest {

    @Test
    void booking_ShouldWorkCorrectly() {
        Item item = new Item();
        item.setId(1L);

        User booker = new User();
        booker.setId(2L);

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setStart(LocalDateTime.now());
        booking.setEnd(LocalDateTime.now().plusDays(1));
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStatus(BookingStatus.WAITING);

        assertEquals(1L, booking.getId());
        assertNotNull(booking.getStart());
        assertNotNull(booking.getEnd());
        assertEquals(item, booking.getItem());
        assertEquals(booker, booking.getBooker());
        assertEquals(BookingStatus.WAITING, booking.getStatus());
    }

    @Test
    void booking_WithMinDateTime_ShouldWork() {
        Booking booking = new Booking();
        LocalDateTime minDate = LocalDateTime.MIN;

        booking.setStart(minDate);
        booking.setEnd(minDate.plusDays(1));

        assertEquals(minDate, booking.getStart());
        assertEquals(minDate.plusDays(1), booking.getEnd());
    }

    @Test
    void booking_WithMaxDateTime_ShouldWork() {
        Booking booking = new Booking();
        LocalDateTime maxDate = LocalDateTime.MAX;

        booking.setStart(maxDate.minusDays(1));
        booking.setEnd(maxDate);

        assertEquals(maxDate.minusDays(1), booking.getStart());
        assertEquals(maxDate, booking.getEnd());
    }

    @Test
    void booking_WithSameStartAndEndTime_ShouldWork() {
        Booking booking = new Booking();
        LocalDateTime sameTime = LocalDateTime.now();

        booking.setStart(sameTime);
        booking.setEnd(sameTime);

        assertEquals(sameTime, booking.getStart());
        assertEquals(sameTime, booking.getEnd());
    }

    @Test
    void booking_WithNullItem_ShouldWork() {
        Booking booking = new Booking();

        booking.setItem(null);

        assertNull(booking.getItem());
    }

    @Test
    void booking_WithNullBooker_ShouldWork() {
        Booking booking = new Booking();

        booking.setBooker(null);

        assertNull(booking.getBooker());
    }

    @Test
    void booking_WithNullStatus_ShouldWork() {
        Booking booking = new Booking();

        booking.setStatus(null);

        assertNull(booking.getStatus());
    }

    @Test
    void booking_WithAllStatuses_ShouldWork() {
        Booking booking = new Booking();

        for (BookingStatus status : BookingStatus.values()) {
            booking.setStatus(status);
            assertEquals(status, booking.getStatus());
        }
    }

    @Test
    void booking_WithMinId_ShouldWork() {
        Booking booking = new Booking();

        booking.setId(Long.MIN_VALUE);

        assertEquals(Long.MIN_VALUE, booking.getId());
    }

    @Test
    void booking_WithMaxId_ShouldWork() {
        Booking booking = new Booking();

        booking.setId(Long.MAX_VALUE);

        assertEquals(Long.MAX_VALUE, booking.getId());
    }

    @Test
    void booking_WithZeroId_ShouldWork() {
        Booking booking = new Booking();

        booking.setId(0L);

        assertEquals(0L, booking.getId());
    }

    @Test
    void booking_WithNegativeId_ShouldWork() {
        Booking booking = new Booking();

        booking.setId(-1L);

        assertEquals(-1L, booking.getId());
    }

    @Test
    void equals_WithNullId_ShouldReturnFalse() {
        Booking booking1 = new Booking();
        booking1.setId(null);

        Booking booking2 = new Booking();
        booking2.setId(1L);

        assertNotEquals(booking1, booking2);
    }

    @Test
    void hashCode_WithNullId_ShouldBeConsistent() {
        Booking booking = new Booking();
        booking.setId(null);

        int initialHashCode = booking.hashCode();

        assertEquals(initialHashCode, booking.hashCode());
    }

    @Test
    void hashCode_WithZeroId_ShouldBeConsistent() {
        Booking booking = new Booking();
        booking.setId(0L);

        int initialHashCode = booking.hashCode();

        assertEquals(initialHashCode, booking.hashCode());
    }

    @Test
    void booking_WithVeryShortDuration_ShouldWork() {
        Booking booking = new Booking();
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusNanos(1); // 1 наносекунда

        booking.setStart(start);
        booking.setEnd(end);

        assertEquals(start, booking.getStart());
        assertEquals(end, booking.getEnd());
        assertTrue(booking.getEnd().isAfter(booking.getStart()));
    }

    @Test
    void booking_WithVeryLongDuration_ShouldWork() {
        Booking booking = new Booking();
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusYears(100); // 100 лет

        booking.setStart(start);
        booking.setEnd(end);

        assertEquals(start, booking.getStart());
        assertEquals(end, booking.getEnd());
        assertTrue(booking.getEnd().isAfter(booking.getStart()));
    }

    @Test
    void booking_WithEndBeforeStart_ShouldWork() {
        Booking booking = new Booking();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = now.plusDays(1);
        LocalDateTime end = now;

        booking.setStart(start);
        booking.setEnd(end);

        assertEquals(start, booking.getStart());
        assertEquals(end, booking.getEnd());
        assertTrue(booking.getEnd().isBefore(booking.getStart()));
    }

    @Test
    void booking_WithItemHavingMinId_ShouldWork() {
        Item item = new Item();
        item.setId(Long.MIN_VALUE);

        Booking booking = new Booking();

        booking.setItem(item);

        assertEquals(item, booking.getItem());
        assertEquals(Long.MIN_VALUE, booking.getItem().getId());
    }

    @Test
    void booking_WithBookerHavingMaxId_ShouldWork() {
        User booker = new User();
        booker.setId(Long.MAX_VALUE);

        Booking booking = new Booking();

        booking.setBooker(booker);

        assertEquals(booker, booking.getBooker());
        assertEquals(Long.MAX_VALUE, booking.getBooker().getId());
    }

    @Test
    void toString_ShouldNotThrowException() {
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setStart(LocalDateTime.now());
        booking.setEnd(LocalDateTime.now().plusDays(1));
        booking.setStatus(BookingStatus.APPROVED);

        assertDoesNotThrow(booking::toString);
        assertNotNull(booking.toString());
    }

    @Test
    void hashCode_ShouldChangeWhenFieldsChange() {
        Booking booking = new Booking();
        booking.setId(1L);
        int initialHashCode = booking.hashCode();

        booking.setId(2L);

        assertNotEquals(initialHashCode, booking.hashCode());
    }
}