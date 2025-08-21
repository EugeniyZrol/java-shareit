package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({RequestServiceImpl.class, RequestMapperImpl.class})
class RequestServiceImplTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private RequestServiceImpl requestService;

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private UserRepository userRepository;

    private User user1;
    private User user2;
    private RequestDto requestDto;

    @BeforeEach
    void setUp() {
        user1 = new User();
        user1.setName("User 1");
        user1.setEmail("user1@email.com");
        user1 = userRepository.save(user1);

        user2 = new User();
        user2.setName("User 2");
        user2.setEmail("user2@email.com");
        user2 = userRepository.save(user2);

        requestDto = new RequestDto();
        requestDto.setDescription("Нужна дрель");
    }

    @Test
    void create_WhenValidRequest_ShouldCreateRequest() {

        RequestDto result = requestService.create(requestDto, user1.getId());

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals("Нужна дрель", result.getDescription());
        assertNotNull(result.getCreated());
        assertNotNull(result.getItems());
        assertTrue(result.getItems().isEmpty());
    }

    @Test
    void create_WhenUserNotFound_ShouldThrowException() {

        assertThrows(NotFoundException.class, () ->
                requestService.create(requestDto, 999L));
    }

    @Test
    void getOwnRequests_WhenUserHasRequests_ShouldReturnRequests() {

        requestService.create(requestDto, user1.getId());

        List<RequestDto> result = requestService.getOwnRequests(user1.getId());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Нужна дрель", result.getFirst().getDescription());
        assertNotNull(result.getFirst().getItems());
        assertTrue(result.getFirst().getItems().isEmpty());
    }

    @Test
    void getOwnRequests_WhenUserNotFound_ShouldThrowException() {

        assertThrows(NotFoundException.class, () ->
                requestService.getOwnRequests(999L));
    }

    @Test
    void getOwnRequests_WhenNoRequests_ShouldReturnEmptyList() {

        List<RequestDto> result = requestService.getOwnRequests(user1.getId());

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getAllRequests_WithPagination_ShouldReturnOtherUsersRequests() {

        requestService.create(requestDto, user1.getId());

        RequestDto requestDto2 = new RequestDto();
        requestDto2.setDescription("Нужен молоток");
        requestService.create(requestDto2, user2.getId());

        List<RequestDto> result = requestService.getAllRequests(user1.getId(), 0, 10);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Нужен молоток", result.getFirst().getDescription());
        assertNotNull(result.getFirst().getItems());
        assertTrue(result.getFirst().getItems().isEmpty());
    }

    @Test
    void getAllRequests_WithoutPagination_ShouldReturnAllOtherUsersRequests() {

        requestService.create(requestDto, user1.getId());

        RequestDto requestDto2 = new RequestDto();
        requestDto2.setDescription("Нужен молоток");
        requestService.create(requestDto2, user2.getId());

        List<RequestDto> result = requestService.getAllRequests(user1.getId(), null, null);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Нужен молоток", result.getFirst().getDescription());
        assertNotNull(result.getFirst().getItems());
        assertTrue(result.getFirst().getItems().isEmpty());
    }

    @Test
    void getAllRequests_WhenUserNotFound_ShouldThrowException() {

        assertThrows(NotFoundException.class, () ->
                requestService.getAllRequests(999L, 0, 10));
    }

    @Test
    void getRequestById_WhenRequestExists_ShouldReturnRequest() {

        RequestDto createdRequest = requestService.create(requestDto, user1.getId());

        RequestDto result = requestService.getRequestById(createdRequest.getId(), user2.getId());

        assertNotNull(result);
        assertEquals(createdRequest.getId(), result.getId());
        assertEquals("Нужна дрель", result.getDescription());
        assertNotNull(result.getItems());
        assertTrue(result.getItems().isEmpty());
    }

    @Test
    void getRequestById_WhenRequestNotFound_ShouldThrowException() {

        assertThrows(NotFoundException.class, () ->
                requestService.getRequestById(999L, user1.getId()));
    }

    @Test
    void getRequestById_WhenUserNotFound_ShouldThrowException() {

        RequestDto createdRequest = requestService.create(requestDto, user1.getId());

        assertThrows(NotFoundException.class, () ->
                requestService.getRequestById(createdRequest.getId(), 999L));
    }

    @Test
    void create_ShouldSetCorrectTimestamp() {

        LocalDateTime beforeCreate = LocalDateTime.now().minusSeconds(1);

        RequestDto result = requestService.create(requestDto, user1.getId());

        assertNotNull(result.getCreated());
        assertTrue(result.getCreated().isAfter(beforeCreate));
        assertTrue(result.getCreated().isBefore(LocalDateTime.now().plusSeconds(1)));
    }

    @Test
    void getRequestById_WithItems_ShouldReturnRequestWithItems() {

        RequestDto createdRequest = requestService.create(requestDto, user1.getId());

        Item item = new Item();
        item.setName("Дрель");
        item.setDescription("Мощная дрель");
        item.setAvailable(true);
        item.setOwnerId(user2.getId());

        Request request = requestRepository.findById(createdRequest.getId()).get();
        item.setRequest(request);

        entityManager.persist(item);
        entityManager.flush();
        entityManager.clear();

        RequestDto result = requestService.getRequestById(createdRequest.getId(), user2.getId());

        assertNotNull(result.getItems());
        assertNotNull(result.getItems());

        Request refreshedRequest = requestRepository.findById(createdRequest.getId()).get();
        assertNotNull(refreshedRequest.getItems());
    }

    @Test
    void getRequestById_WithoutItems_ShouldReturnEmptyItemsList() {

        RequestDto createdRequest = requestService.create(requestDto, user1.getId());

        RequestDto result = requestService.getRequestById(createdRequest.getId(), user2.getId());

        assertNotNull(result.getItems());
        assertTrue(result.getItems().isEmpty());
    }

    @Test
    void getAllRequests_WhenNoOtherUsersRequests_ShouldReturnEmptyList() {

        requestService.create(requestDto, user1.getId());

        List<RequestDto> result = requestService.getAllRequests(user1.getId(), 0, 10);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}