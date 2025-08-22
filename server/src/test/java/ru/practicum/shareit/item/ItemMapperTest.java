package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.request.Request;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ItemMapperTest {

    private final ItemMapper itemMapper = Mappers.getMapper(ItemMapper.class);

    @Test
    void toItem_ShouldMapCorrectly() {
        ItemRequest itemRequest = new ItemRequest("Test Item", "Test Description", true, 1L);

        Item item = itemMapper.toItem(itemRequest);

        assertNotNull(item);
        assertNull(item.getId());
        assertEquals("Test Item", item.getName());
        assertEquals("Test Description", item.getDescription());
        assertTrue(item.getAvailable());
        assertNull(item.getOwnerId());
        assertNull(item.getRequest());
        assertNull(item.getComments());
    }

    @Test
    void toItemResponse_WithBookings_ShouldMapCorrectly() {
        Item item = createTestItem();
        List<Booking> bookings = createTestBookings();

        ItemResponse response = itemMapper.toItemResponse(item, bookings);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Test Item", response.getName());
        assertEquals("Test Description", response.getDescription());
        assertTrue(response.getAvailable());
        assertEquals(1L, response.getOwnerId());
        assertEquals(1L, response.getRequestId());

        assertNotNull(response.getLastBooking());
        assertEquals(2L, response.getLastBooking().getId());
        assertEquals(2L, response.getLastBooking().getBookerId());

        assertNotNull(response.getNextBooking());
        assertEquals(3L, response.getNextBooking().getId());
        assertEquals(3L, response.getNextBooking().getBookerId());
    }

    @Test
    void toItemResponse_WithoutBookings_ShouldMapCorrectly() {
        Item item = createTestItem();

        ItemResponse response = itemMapper.toItemResponse(item, List.of());

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Test Item", response.getName());
        assertEquals("Test Description", response.getDescription());
        assertTrue(response.getAvailable());
        assertEquals(1L, response.getOwnerId());
        assertEquals(1L, response.getRequestId());
        assertNull(response.getLastBooking());
        assertNull(response.getNextBooking());
    }

    @Test
    void toItemResponse_Simple_ShouldMapCorrectly() {
        Item item = createTestItem();

        ItemResponse response = itemMapper.toItemResponse(item);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Test Item", response.getName());
        assertEquals("Test Description", response.getDescription());
        assertTrue(response.getAvailable());
        assertEquals(1L, response.getOwnerId());
        assertEquals(1L, response.getRequestId());
        assertNull(response.getLastBooking());
        assertNull(response.getNextBooking());
        assertNull(response.getComments());
    }

    @Test
    void toCommentDto_ShouldMapCorrectly() {
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setText("Great item!");

        User author = new User();
        author.setId(1L);
        author.setName("John Doe");
        comment.setAuthor(author);

        comment.setCreated(LocalDateTime.now());

        CommentDto dto = itemMapper.toCommentDto(comment);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("Great item!", dto.getText());
        assertEquals("John Doe", dto.getAuthorName());
        assertNotNull(dto.getCreated());
    }

    @Test
    void toComment_ShouldMapCorrectly() {
        CommentDto commentDto = new CommentDto();
        commentDto.setText("Test comment");

        Item item = createTestItem();

        User author = new User();
        author.setId(1L);
        author.setName("Test User");

        Comment comment = itemMapper.toComment(commentDto, item, author);

        assertNotNull(comment);
        assertNull(comment.getId());
        assertEquals("Test comment", comment.getText());
        assertEquals(item, comment.getItem());
        assertEquals(author, comment.getAuthor());
        assertNotNull(comment.getCreated());
    }

    @Test
    void mapLastBooking_ShouldReturnCorrectBooking() {
        List<Booking> bookings = createTestBookings();

        ItemResponse.BookingInfo lastBooking = itemMapper.mapLastBooking(bookings);

        assertNotNull(lastBooking);
        assertEquals(2L, lastBooking.getId()); // Самое позднее прошедшее бронирование
        assertEquals(2L, lastBooking.getBookerId());
    }

    @Test
    void mapNextBooking_ShouldReturnCorrectBooking() {
        List<Booking> bookings = createTestBookings();

        ItemResponse.BookingInfo nextBooking = itemMapper.mapNextBooking(bookings);

        assertNotNull(nextBooking);
        assertEquals(3L, nextBooking.getId()); // Самое раннее будущее бронирование
        assertEquals(3L, nextBooking.getBookerId());
    }

    @Test
    void mapLastBooking_WithEmptyList_ShouldReturnNull() {
        assertNull(itemMapper.mapLastBooking(List.of()));
        assertNull(itemMapper.mapLastBooking(null));
    }

    @Test
    void mapNextBooking_WithEmptyList_ShouldReturnNull() {
        assertNull(itemMapper.mapNextBooking(List.of()));
        assertNull(itemMapper.mapNextBooking(null));
    }

    private Item createTestItem() {
        Item item = new Item();
        item.setId(1L);
        item.setName("Test Item");
        item.setDescription("Test Description");
        item.setAvailable(true);
        item.setOwnerId(1L);

        Request request = new Request();
        request.setId(1L);
        item.setRequest(request);

        return item;
    }

    private List<Booking> createTestBookings() {
        User booker1 = new User();
        booker1.setId(1L);

        User booker2 = new User();
        booker2.setId(2L);

        User booker3 = new User();
        booker3.setId(3L);

        Booking booking1 = new Booking();
        booking1.setId(1L);
        booking1.setStart(LocalDateTime.now().minusDays(10));
        booking1.setEnd(LocalDateTime.now().minusDays(5));
        booking1.setBooker(booker1);
        booking1.setStatus(BookingStatus.APPROVED);

        Booking booking2 = new Booking();
        booking2.setId(2L);
        booking2.setStart(LocalDateTime.now().minusDays(3));
        booking2.setEnd(LocalDateTime.now().minusDays(1));
        booking2.setBooker(booker2);
        booking2.setStatus(BookingStatus.APPROVED);

        Booking booking3 = new Booking();
        booking3.setId(3L);
        booking3.setStart(LocalDateTime.now().plusDays(1));
        booking3.setEnd(LocalDateTime.now().plusDays(5));
        booking3.setBooker(booker3);
        booking3.setStatus(BookingStatus.APPROVED);

        Booking booking4 = new Booking();
        booking4.setId(4L);
        booking4.setStart(LocalDateTime.now().plusDays(10));
        booking4.setEnd(LocalDateTime.now().plusDays(15));
        booking4.setBooker(booker1);
        booking4.setStatus(BookingStatus.APPROVED);

        return List.of(booking1, booking2, booking3, booking4);
    }
}