package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({BookingServiceImpl.class, BookingMapperImpl.class})
class BookingServiceImplTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BookingServiceImpl bookingService;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    private User owner;
    private User booker;
    private Item item;
    private BookingRequestDto bookingRequestDto;

    @BeforeEach
    void setUp() {
        owner = new User();
        owner.setName("Owner");
        owner.setEmail("owner@email.com");
        owner = userRepository.save(owner);

        booker = new User();
        booker.setName("Booker");
        booker.setEmail("booker@email.com");
        booker = userRepository.save(booker);

        item = new Item();
        item.setName("Test Item");
        item.setDescription("Test Description");
        item.setAvailable(true);
        item.setOwnerId(owner.getId());
        item = itemRepository.save(item);

        bookingRequestDto = new BookingRequestDto();
        bookingRequestDto.setItemId(item.getId());
        bookingRequestDto.setStart(LocalDateTime.now().plusDays(1));
        bookingRequestDto.setEnd(LocalDateTime.now().plusDays(2));
    }

    @Test
    void createBooking_WhenValidRequest_ShouldCreateBooking() {

        BookingResponseDto result = bookingService.createBooking(bookingRequestDto, booker.getId());

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(BookingStatus.WAITING, result.getStatus());
        assertEquals(booker.getId(), result.getBooker().getId());
        assertEquals(item.getId(), result.getItem().getId());
    }

    @Test
    void createBooking_WhenItemNotAvailable_ShouldThrowException() {

        item.setAvailable(false);
        itemRepository.save(item);

        assertThrows(ItemNotAvailableException.class, () ->
                bookingService.createBooking(bookingRequestDto, booker.getId()));
    }

    @Test
    void createBooking_WhenSelfBooking_ShouldThrowException() {

        assertThrows(SelfBookingException.class, () ->
                bookingService.createBooking(bookingRequestDto, owner.getId()));
    }

    @Test
    void createBooking_WhenUserNotFound_ShouldThrowException() {

        assertThrows(NotFoundException.class, () ->
                bookingService.createBooking(bookingRequestDto, 999L));
    }

    @Test
    void createBooking_WhenItemNotFound_ShouldThrowException() {

        bookingRequestDto.setItemId(999L);

        assertThrows(NotFoundException.class, () ->
                bookingService.createBooking(bookingRequestDto, booker.getId()));
    }

    @Test
    void createBooking_WhenInvalidTime_ShouldThrowException() {

        bookingRequestDto.setStart(LocalDateTime.now().plusDays(2));
        bookingRequestDto.setEnd(LocalDateTime.now().plusDays(1));

        assertThrows(InvalidBookingTimeException.class, () ->
                bookingService.createBooking(bookingRequestDto, booker.getId()));
    }

    @Test
    void approveBooking_WhenValidRequest_ShouldApproveBooking() {

        BookingResponseDto createdBooking = bookingService.createBooking(bookingRequestDto, booker.getId());

        BookingResponseDto result = bookingService.approveBooking(createdBooking.getId(), true, owner.getId());

        assertNotNull(result);
        assertEquals(BookingStatus.APPROVED, result.getStatus());
    }

    @Test
    void approveBooking_WhenNotOwner_ShouldThrowException() {

        BookingResponseDto createdBooking = bookingService.createBooking(bookingRequestDto, booker.getId());

        assertThrows(NotOwnerException.class, () ->
                bookingService.approveBooking(createdBooking.getId(), true, booker.getId()));
    }

    @Test
    void approveBooking_WhenAlreadyProcessed_ShouldThrowException() {

        BookingResponseDto createdBooking = bookingService.createBooking(bookingRequestDto, booker.getId());
        bookingService.approveBooking(createdBooking.getId(), true, owner.getId());

        assertThrows(AlreadyProcessedException.class, () ->
                bookingService.approveBooking(createdBooking.getId(), false, owner.getId()));
    }

    @Test
    void getBookingById_WhenUserIsBooker_ShouldReturnBooking() {

        BookingResponseDto createdBooking = bookingService.createBooking(bookingRequestDto, booker.getId());

        BookingResponseDto result = bookingService.getBookingById(createdBooking.getId(), booker.getId());

        assertNotNull(result);
        assertEquals(createdBooking.getId(), result.getId());
    }

    @Test
    void getBookingById_WhenUserIsOwner_ShouldReturnBooking() {

        BookingResponseDto createdBooking = bookingService.createBooking(bookingRequestDto, booker.getId());

        BookingResponseDto result = bookingService.getBookingById(createdBooking.getId(), owner.getId());

        assertNotNull(result);
        assertEquals(createdBooking.getId(), result.getId());
    }

    @Test
    void getUserBookings_ShouldReturnUserBookings() {

        bookingService.createBooking(bookingRequestDto, booker.getId());

        List<BookingResponseDto> result = bookingService.getUserBookings("ALL", booker.getId(), 0, 10);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void getOwnerBookings_ShouldReturnOwnerBookings() {

        bookingService.createBooking(bookingRequestDto, booker.getId());

        List<BookingResponseDto> result = bookingService.getOwnerBookings("ALL", owner.getId(), 0, 10);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void getUserBookings_WithDifferentStates_ShouldReturnFilteredBookings() {

        bookingService.createBooking(bookingRequestDto, booker.getId());

        assertDoesNotThrow(() -> {
            bookingService.getUserBookings("WAITING", booker.getId(), 0, 10);
            bookingService.getUserBookings("FUTURE", booker.getId(), 0, 10);
        });
    }

    @Test
    void getUserBookings_WithInvalidState_ShouldThrowException() {

        assertThrows(UnsupportedStatusException.class, () ->
                bookingService.getUserBookings("INVALID", booker.getId(), 0, 10));
    }

    @Test
    void getUserBookings_WithInvalidPagination_ShouldThrowException() {

        assertThrows(ValidationException.class, () ->
                bookingService.getUserBookings("ALL", booker.getId(), -1, 10));
    }
}