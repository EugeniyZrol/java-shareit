package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.comment.CommentDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ItemResponseTest {

    @Test
    void itemResponse_ShouldWorkCorrectly() {
        ItemResponse.BookingInfo lastBooking = new ItemResponse.BookingInfo(1L, 2L);
        ItemResponse.BookingInfo nextBooking = new ItemResponse.BookingInfo(3L, 4L);

        CommentDto comment1 = new CommentDto();
        comment1.setId(1L);
        comment1.setText("Great!");
        comment1.setAuthorName("User1");
        comment1.setCreated(LocalDateTime.now());

        CommentDto comment2 = new CommentDto();
        comment2.setId(2L);
        comment2.setText("Awesome!");
        comment2.setAuthorName("User2");
        comment2.setCreated(LocalDateTime.now().minusDays(1));

        ItemResponse response = new ItemResponse(
                1L, "Test Item", "Test Description", true, 5L, 10L,
                lastBooking, nextBooking, List.of(comment1, comment2)
        );

        assertEquals(1L, response.getId());
        assertEquals("Test Item", response.getName());
        assertEquals("Test Description", response.getDescription());
        assertTrue(response.getAvailable());
        assertEquals(5L, response.getOwnerId());
        assertEquals(10L, response.getRequestId());

        assertNotNull(response.getLastBooking());
        assertEquals(1L, response.getLastBooking().getId());
        assertEquals(2L, response.getLastBooking().getBookerId());

        assertNotNull(response.getNextBooking());
        assertEquals(3L, response.getNextBooking().getId());
        assertEquals(4L, response.getNextBooking().getBookerId());

        assertNotNull(response.getComments());
        assertEquals(2, response.getComments().size());
        assertEquals("Great!", response.getComments().get(0).getText());
        assertEquals("Awesome!", response.getComments().get(1).getText());
    }

    @Test
    void itemResponse_NoArgsConstructor_ShouldWork() {
        ItemResponse response = new ItemResponse();

        assertNull(response.getId());
        assertNull(response.getName());
        assertNull(response.getDescription());
        assertNull(response.getAvailable());
        assertNull(response.getOwnerId());
        assertNull(response.getRequestId());
        assertNull(response.getLastBooking());
        assertNull(response.getNextBooking());
        assertNull(response.getComments());
    }

    @Test
    void bookingInfo_ShouldWorkCorrectly() {
        ItemResponse.BookingInfo bookingInfo = new ItemResponse.BookingInfo(1L, 2L);

        assertEquals(1L, bookingInfo.getId());
        assertEquals(2L, bookingInfo.getBookerId());
    }

    @Test
    void bookingInfo_NoArgsConstructor_ShouldWork() {
        ItemResponse.BookingInfo bookingInfo = new ItemResponse.BookingInfo();

        assertNull(bookingInfo.getId());
        assertNull(bookingInfo.getBookerId());
    }

    @Test
    void bookingInfo_SettersAndGetters_ShouldWork() {
        ItemResponse.BookingInfo bookingInfo = new ItemResponse.BookingInfo();

        bookingInfo.setId(5L);
        bookingInfo.setBookerId(10L);

        assertEquals(5L, bookingInfo.getId());
        assertEquals(10L, bookingInfo.getBookerId());
    }

    @Test
    void itemResponse_SettersAndGetters_ShouldWork() {
        ItemResponse response = new ItemResponse();
        ItemResponse.BookingInfo lastBooking = new ItemResponse.BookingInfo(1L, 2L);
        ItemResponse.BookingInfo nextBooking = new ItemResponse.BookingInfo(3L, 4L);

        response.setId(10L);
        response.setName("New Item");
        response.setDescription("New Description");
        response.setAvailable(false);
        response.setOwnerId(20L);
        response.setRequestId(30L);
        response.setLastBooking(lastBooking);
        response.setNextBooking(nextBooking);
        response.setComments(List.of());

        assertEquals(10L, response.getId());
        assertEquals("New Item", response.getName());
        assertEquals("New Description", response.getDescription());
        assertFalse(response.getAvailable());
        assertEquals(20L, response.getOwnerId());
        assertEquals(30L, response.getRequestId());
        assertEquals(lastBooking, response.getLastBooking());
        assertEquals(nextBooking, response.getNextBooking());
        assertNotNull(response.getComments());
        assertTrue(response.getComments().isEmpty());
    }
}