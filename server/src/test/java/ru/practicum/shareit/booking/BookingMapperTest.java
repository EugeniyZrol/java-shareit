package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemResponse;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserResponse;

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
    void toBookingResponseDto_WithNullBooking_ShouldReturnNull() {
        assertNull(bookingMapper.toBookingResponseDto(null));
    }

    @Test
    void toBooking_ShouldMapCorrectly() {
        BookingRequestDto requestDto = new BookingRequestDto();
        requestDto.setItemId(1L);
        requestDto.setStart(LocalDateTime.now());
        requestDto.setEnd(LocalDateTime.now().plusDays(1));

        Booking booking = bookingMapper.toBooking(requestDto, 1L);

        assertNotNull(booking);
        assertNull(booking.getId());
        assertEquals(BookingStatus.WAITING, booking.getStatus());
        assertNotNull(booking.getItem());
        assertEquals(1L, booking.getItem().getId());
        assertNotNull(booking.getBooker());
        assertEquals(1L, booking.getBooker().getId());
        assertEquals(requestDto.getStart(), booking.getStart());
        assertEquals(requestDto.getEnd(), booking.getEnd());
    }

    @Test
    void toBooking_WithNullRequest_ShouldReturnNull() {

        assertNull(bookingMapper.toBooking(null, 1L));
    }

    @Test
    void toBooking_WithNullBookerId_ShouldHaveNullBooker() {
        BookingRequestDto requestDto = new BookingRequestDto();
        requestDto.setItemId(1L);
        requestDto.setStart(LocalDateTime.now());
        requestDto.setEnd(LocalDateTime.now().plusDays(1));

        Booking booking = bookingMapper.toBooking(requestDto, null);

        assertNotNull(booking);
        assertNull(booking.getBooker());
        assertNotNull(booking.getItem());
        assertEquals(1L, booking.getItem().getId());
    }

    @Test
    void toBooking_WithNullItemId_ShouldHaveNullItem() {
        BookingRequestDto requestDto = new BookingRequestDto();
        requestDto.setItemId(null);
        requestDto.setStart(LocalDateTime.now());
        requestDto.setEnd(LocalDateTime.now().plusDays(1));

        Booking booking = bookingMapper.toBooking(requestDto, 1L);

        assertNotNull(booking);
        assertNull(booking.getItem());
        assertNotNull(booking.getBooker());
        assertEquals(1L, booking.getBooker().getId());
    }

    @Test
    void mapIdToItem_WithValidId_ShouldReturnItemWithId() {
        Item item = bookingMapper.mapIdToItem(1L);

        assertNotNull(item);
        assertEquals(1L, item.getId());
        assertNull(item.getName());
        assertNull(item.getDescription());
        assertNull(item.getAvailable());
    }

    @Test
    void mapIdToItem_WithNullId_ShouldReturnNull() {
        assertNull(bookingMapper.mapIdToItem(null));
    }

    @Test
    void mapIdToUser_WithValidId_ShouldReturnUserWithId() {
        User user = bookingMapper.mapIdToUser(1L);

        assertNotNull(user);
        assertEquals(1L, user.getId());
        assertNull(user.getName());
        assertNull(user.getEmail());
    }

    @Test
    void mapIdToUser_WithNullId_ShouldReturnNull() {
        assertNull(bookingMapper.mapIdToUser(null));
    }

    @Test
    void mapItemToDto_WithValidItem_ShouldReturnDto() {
        Item item = new Item();
        item.setId(1L);
        item.setName("Test Item");
        item.setDescription("Test Description");
        item.setAvailable(true);

        ItemResponse dto = bookingMapper.mapItemToDto(item);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("Test Item", dto.getName());
        assertNull(dto.getDescription());
        assertNull(dto.getAvailable());
    }

    @Test
    void mapItemToDto_WithNullItem_ShouldReturnNull() {
        assertNull(bookingMapper.mapItemToDto(null));
    }

    @Test
    void mapItemToDto_WithItemHavingNullFields_ShouldReturnDto() {
        Item item = new Item();
        item.setId(1L);

        ItemResponse dto = bookingMapper.mapItemToDto(item);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertNull(dto.getName());
    }

    @Test
    void mapUserToDto_WithValidUser_ShouldReturnDto() {
        User user = new User();
        user.setId(1L);
        user.setName("Test User");
        user.setEmail("test@email.com");

        UserResponse dto = bookingMapper.mapUserToDto(user);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertNull(dto.getName());
        assertNull(dto.getEmail());
    }

    @Test
    void mapUserToDto_WithNullUser_ShouldReturnNull() {
        assertNull(bookingMapper.mapUserToDto(null));
    }

    @Test
    void mapUserToDto_WithUserHavingNullFields_ShouldReturnDto() {
        User user = new User();
        user.setId(1L);

        UserResponse dto = bookingMapper.mapUserToDto(user);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertNull(dto.getName());
    }

    @Test
    void toBooking_WithAllNullFieldsInRequest_ShouldCreateBookingWithNullFields() {
        BookingRequestDto requestDto = new BookingRequestDto();

        Booking booking = bookingMapper.toBooking(requestDto, null);

        assertNotNull(booking);
        assertNull(booking.getId());
        assertNull(booking.getStart());
        assertNull(booking.getEnd());
        assertEquals(BookingStatus.WAITING, booking.getStatus());
        assertNull(booking.getItem());
        assertNull(booking.getBooker());
    }

    @Test
    void toBooking_WithPartialNullFieldsInRequest_ShouldHandleCorrectly() {
        BookingRequestDto requestDto = new BookingRequestDto();
        requestDto.setItemId(1L);

        Booking booking = bookingMapper.toBooking(requestDto, 1L);

        assertNotNull(booking);
        assertNull(booking.getStart());
        assertNull(booking.getEnd());
        assertNotNull(booking.getItem());
        assertEquals(1L, booking.getItem().getId());
        assertNotNull(booking.getBooker());
        assertEquals(1L, booking.getBooker().getId());
    }
}