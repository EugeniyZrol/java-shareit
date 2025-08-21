package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.comment.CommentRepository;
import ru.practicum.shareit.request.RequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({ItemServiceImpl.class, ItemMapperImpl.class})
class ItemServiceImplTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ItemServiceImpl itemService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private RequestRepository requestRepository;

    private User owner;
    private User booker;
    private Item item;

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
    }

    @Test
    void getItemById_WhenItemExists_ShouldReturnItem() {

        ItemResponse result = itemService.getItemById(item.getId(), owner.getId());

        assertNotNull(result);
        assertEquals(item.getId(), result.getId());
        assertEquals(item.getName(), result.getName());
        assertEquals(item.getDescription(), result.getDescription());
    }

    @Test
    void getItemById_WhenItemNotExists_ShouldThrowException() {

        assertThrows(NotFoundException.class, () ->
                itemService.getItemById(999L, owner.getId()));
    }

    @Test
    void getAllItemsByOwner_WhenOwnerHasItems_ShouldReturnItems() {

        List<ItemResponse> result = itemService.getAllItemsByOwner(owner.getId());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(item.getId(), result.get(0).getId());
    }

    @Test
    void getAllItemsByOwner_WhenOwnerHasNoItems_ShouldReturnEmptyList() {

        List<ItemResponse> result = itemService.getAllItemsByOwner(999L);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void addItem_WhenValidRequest_ShouldCreateItem() {

        ItemRequest request = new ItemRequest("New Item", "New Description", true, null);

        ItemResponse result = itemService.addItem(request, owner.getId());

        assertNotNull(result);
        assertEquals("New Item", result.getName());
        assertEquals("New Description", result.getDescription());
        assertTrue(result.getAvailable());
        assertEquals(owner.getId(), result.getOwnerId());
    }

    @Test
    void updateItem_WhenValidUpdate_ShouldUpdateItem() {

        ItemRequest updateRequest = new ItemRequest("Updated Name", "Updated Desc", false, null);

        ItemResponse result = itemService.updateItem(item.getId(), updateRequest, owner.getId());

        assertNotNull(result);
        assertEquals("Updated Name", result.getName());
        assertEquals("Updated Desc", result.getDescription());
        assertFalse(result.getAvailable());
    }

    @Test
    void searchAvailableItems_WhenTextMatches_ShouldReturnItems() {

        List<ItemResponse> result = itemService.searchAvailableItems("test");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(item.getId(), result.get(0).getId());
    }

    @Test
    void searchAvailableItems_WhenTextBlank_ShouldReturnEmptyList() {

        List<ItemResponse> result = itemService.searchAvailableItems("");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void addComment_WhenValidComment_ShouldCreateComment() {

        Booking booking = new Booking();
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStart(LocalDateTime.now().minusDays(2));
        booking.setEnd(LocalDateTime.now().minusDays(1));
        booking.setStatus(BookingStatus.APPROVED);
        bookingRepository.save(booking);

        CommentDto commentDto = new CommentDto();
        commentDto.setText("Great item!");

        CommentDto result = itemService.addComment(item.getId(), commentDto, booker.getId());

        assertNotNull(result);
        assertEquals("Great item!", result.getText());
        assertEquals("Booker", result.getAuthorName());
        assertNotNull(result.getCreated());
    }

    @Test
    void addComment_WhenUserNeverBooked_ShouldThrowException() {

        CommentDto commentDto = new CommentDto();
        commentDto.setText("Great item!");

        assertThrows(ValidationException.class, () ->
                itemService.addComment(item.getId(), commentDto, booker.getId()));
    }
}