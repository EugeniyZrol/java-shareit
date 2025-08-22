// BookingStateTest.java
package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class BookingStateTest {

    @Test
    void fromString_ShouldReturnCorrectState() {

        assertEquals(Optional.of(BookingState.ALL), BookingState.fromString("ALL"));
        assertEquals(Optional.of(BookingState.CURRENT), BookingState.fromString("CURRENT"));
        assertEquals(Optional.of(BookingState.PAST), BookingState.fromString("PAST"));
        assertEquals(Optional.of(BookingState.FUTURE), BookingState.fromString("FUTURE"));
        assertEquals(Optional.of(BookingState.WAITING), BookingState.fromString("WAITING"));
        assertEquals(Optional.of(BookingState.REJECTED), BookingState.fromString("REJECTED"));
    }

    @Test
    void fromString_WithLowerCase_ShouldReturnCorrectState() {

        assertEquals(Optional.of(BookingState.ALL), BookingState.fromString("all"));
        assertEquals(Optional.of(BookingState.CURRENT), BookingState.fromString("current"));
    }

    @Test
    void fromString_WithInvalidState_ShouldReturnEmpty() {

        assertEquals(Optional.empty(), BookingState.fromString("INVALID"));
        assertEquals(Optional.empty(), BookingState.fromString(""));
        assertEquals(Optional.empty(), BookingState.fromString(null));
    }
}