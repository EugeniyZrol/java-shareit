package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.practicum.shareit.exception.DuplicatedDataException;
import ru.practicum.shareit.exception.NotFoundException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({UserServiceImpl.class, UserMapperImpl.class})
class UserServiceImplTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private UserRepository userRepository;

    private UserRequest userRequest;

    @BeforeEach
    void setUp() {
        userRequest = new UserRequest();
        userRequest.setName("Test User");
        userRequest.setEmail("test@email.com");
    }

    @Test
    void create_WhenValidRequest_ShouldCreateUser() {

        UserResponse result = userService.create(userRequest);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals("Test User", result.getName());
        assertEquals("test@email.com", result.getEmail());
    }

    @Test
    void create_WhenDuplicateEmail_ShouldThrowException() {

        userService.create(userRequest);

        assertThrows(DuplicatedDataException.class, () -> userService.create(userRequest));
    }

    @Test
    void update_WhenValidUpdate_ShouldUpdateUser() {

        UserResponse createdUser = userService.create(userRequest);

        UserRequest updateRequest = new UserRequest();
        updateRequest.setName("Updated User");
        updateRequest.setEmail("updated@email.com");

        UserResponse result = userService.update(createdUser.getId(), updateRequest);

        assertNotNull(result);
        assertEquals(createdUser.getId(), result.getId());
        assertEquals("Updated User", result.getName());
        assertEquals("updated@email.com", result.getEmail());
    }

    @Test
    void update_WhenUserNotFound_ShouldThrowException() {

        UserRequest updateRequest = new UserRequest();
        updateRequest.setName("Updated User");

        assertThrows(NotFoundException.class, () -> userService.update(999L, updateRequest));
    }

    @Test
    void update_WhenDuplicateEmail_ShouldThrowException() {

        UserResponse user1 = userService.create(userRequest);

        UserRequest userRequest2 = new UserRequest();
        userRequest2.setName("User 2");
        userRequest2.setEmail("user2@email.com");
        userService.create(userRequest2);

        UserRequest updateRequest = new UserRequest();
        updateRequest.setEmail("user2@email.com"); // Дубликат email

        assertThrows(DuplicatedDataException.class, () ->
                userService.update(user1.getId(), updateRequest));
    }

    @Test
    void getUserById_WhenUserExists_ShouldReturnUser() {

        UserResponse createdUser = userService.create(userRequest);

        UserResponse result = userService.getUserById(createdUser.getId());

        assertNotNull(result);
        assertEquals(createdUser.getId(), result.getId());
        assertEquals("Test User", result.getName());
    }

    @Test
    void getUserById_WhenUserNotExists_ShouldThrowException() {

        assertThrows(NotFoundException.class, () -> userService.getUserById(999L));
    }

    @Test
    void findAll_WhenUsersExist_ShouldReturnAllUsers() {

        userService.create(userRequest);

        UserRequest userRequest2 = new UserRequest();
        userRequest2.setName("User 2");
        userRequest2.setEmail("user2@email.com");
        userService.create(userRequest2);

        List<UserResponse> result = userService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void findAll_WhenNoUsers_ShouldReturnEmptyList() {

        List<UserResponse> result = userService.findAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void delete_WhenUserExists_ShouldDeleteUser() {

        UserResponse createdUser = userService.create(userRequest);

        userService.delete(createdUser.getId());

        assertThrows(NotFoundException.class, () -> userService.getUserById(createdUser.getId()));
    }

    @Test
    void delete_WhenUserNotExists_ShouldThrowException() {

        assertThrows(NotFoundException.class, () -> userService.delete(999L));
    }

    @Test
    void update_WhenPartialUpdate_ShouldUpdateOnlyProvidedFields() {

        UserResponse createdUser = userService.create(userRequest);

        UserRequest updateRequest = new UserRequest();
        updateRequest.setName("Updated Name"); // Только имя

        UserResponse result = userService.update(createdUser.getId(), updateRequest);

        assertNotNull(result);
        assertEquals("Updated Name", result.getName());
        assertEquals("test@email.com", result.getEmail()); // Email остался прежним
    }
}