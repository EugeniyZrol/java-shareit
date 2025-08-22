package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Test
    void toUserResponse_ShouldMapUserToResponseCorrectly() {
        User user = new User();
        user.setId(1L);
        user.setName("Test User");
        user.setEmail("test@email.com");

        UserResponse response = userMapper.toUserResponse(user);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Test User", response.getName());
        assertEquals("test@email.com", response.getEmail());
    }

    @Test
    void toUserResponse_WithNullUser_ShouldReturnNull() {
        UserResponse response = userMapper.toUserResponse(null);

        assertNull(response);
    }

    @Test
    void toUserResponse_WithPartialData_ShouldMapCorrectly() {
        User user = new User();
        user.setId(1L);
        user.setName("Test User");

        UserResponse response = userMapper.toUserResponse(user);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Test User", response.getName());
        assertNull(response.getEmail());
    }

    @Test
    void toUser_ShouldMapUserRequestToUserCorrectly() {
        UserRequest request = new UserRequest();
        request.setName("Test User");
        request.setEmail("test@email.com");

        User user = userMapper.toUser(request);

        assertNotNull(user);
        assertNull(user.getId()); // ID должно игнорироваться
        assertEquals("Test User", user.getName());
        assertEquals("test@email.com", user.getEmail());
    }

    @Test
    void toUser_WithNullRequest_ShouldReturnNull() {
        User user = userMapper.toUser(null);

        assertNull(user);
    }

    @Test
    void toUser_WithPartialData_ShouldMapCorrectly() {
        UserRequest request = new UserRequest();
        request.setName("Test User");

        User user = userMapper.toUser(request);

        assertNotNull(user);
        assertNull(user.getId());
        assertEquals("Test User", user.getName());
        assertNull(user.getEmail());
    }

    @Test
    void toUser_WithEmptyRequest_ShouldCreateEmptyUser() {
        UserRequest request = new UserRequest();

        User user = userMapper.toUser(request);

        assertNotNull(user);
        assertNull(user.getId());
        assertNull(user.getName());
        assertNull(user.getEmail());
    }

    @Test
    void toUser_ShouldIgnoreIdFromRequest() {
        UserRequest request = new UserRequest();
        request.setName("Test User");
        request.setEmail("test@email.com");

        User user = userMapper.toUser(request);

        assertNotNull(user);
        assertNull(user.getId()); // Должно остаться null благодаря @Mapping(target = "id", ignore = true)
        assertEquals("Test User", user.getName());
        assertEquals("test@email.com", user.getEmail());
    }

    @Test
    void toUserResponse_ShouldMapAllFields() {
        User user = new User();
        user.setId(99L);
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");

        UserResponse response = userMapper.toUserResponse(user);

        assertNotNull(response);
        assertEquals(99L, response.getId());
        assertEquals("John Doe", response.getName());
        assertEquals("john.doe@example.com", response.getEmail());
    }

    @Test
    void bidirectionalMapping_ShouldWorkCorrectly() {
        UserRequest request = new UserRequest();
        request.setName("Test User");
        request.setEmail("test@email.com");

        User user = userMapper.toUser(request);
        UserResponse response = userMapper.toUserResponse(user);

        assertNotNull(user);
        assertNotNull(response);
        assertEquals(request.getName(), user.getName());
        assertEquals(request.getEmail(), user.getEmail());
        assertEquals(user.getName(), response.getName());
        assertEquals(user.getEmail(), response.getEmail());
    }

    @Test
    void toUser_WithEmptyStrings_ShouldMapAsEmptyStrings() {
        // Arrange
        UserRequest request = new UserRequest();
        request.setName("");
        request.setEmail("");

        // Act
        User user = userMapper.toUser(request);

        // Assert
        assertNotNull(user);
        assertEquals("", user.getName());
        assertEquals("", user.getEmail());
    }

    @Test
    void toUserResponse_WithEmptyStrings_ShouldMapAsEmptyStrings() {
        // Arrange
        User user = new User();
        user.setId(1L);
        user.setName("");
        user.setEmail("");

        // Act
        UserResponse response = userMapper.toUserResponse(user);

        // Assert
        assertNotNull(response);
        assertEquals("", response.getName());
        assertEquals("", response.getEmail());
    }

    @Test
    void toUser_WithVeryLongStrings_ShouldMapCorrectly() {
        // Arrange
        String longName = "A".repeat(1000);
        String longEmail = "test@" + "a".repeat(245) + ".com"; // Максимально допустимый email

        UserRequest request = new UserRequest();
        request.setName(longName);
        request.setEmail(longEmail);

        // Act
        User user = userMapper.toUser(request);

        // Assert
        assertNotNull(user);
        assertEquals(longName, user.getName());
        assertEquals(longEmail, user.getEmail());
    }

    @Test
    void toUser_WithSpecialCharacters_ShouldMapCorrectly() {
        // Arrange
        UserRequest request = new UserRequest();
        request.setName("User with spaces and-special_chars");
        request.setEmail("user+test@example.com");

        // Act
        User user = userMapper.toUser(request);

        // Assert
        assertNotNull(user);
        assertEquals("User with spaces and-special_chars", user.getName());
        assertEquals("user+test@example.com", user.getEmail());
    }

    @Test
    void toUserResponse_WithSpecialCharacters_ShouldMapCorrectly() {
        // Arrange
        User user = new User();
        user.setId(1L);
        user.setName("User with spaces and-special_chars");
        user.setEmail("user+test@example.com");

        // Act
        UserResponse response = userMapper.toUserResponse(user);

        // Assert
        assertNotNull(response);
        assertEquals("User with spaces and-special_chars", response.getName());
        assertEquals("user+test@example.com", response.getEmail());
    }
}