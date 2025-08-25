package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void user_ShouldWorkCorrectly() {
        User user = new User();
        user.setId(1L);
        user.setName("John Doe");
        user.setEmail("john@example.com");

        assertEquals(1L, user.getId());
        assertEquals("John Doe", user.getName());
        assertEquals("john@example.com", user.getEmail());
    }

    @Test
    void equals_ShouldReturnTrueForSameIdAndEmail() {
        User user1 = new User();
        user1.setId(1L);
        user1.setEmail("test@example.com");

        User user2 = new User();
        user2.setId(1L);
        user2.setEmail("test@example.com");

        assertEquals(user1, user2);
        assertEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    void equals_ShouldReturnFalseForDifferentId() {
        User user1 = new User();
        user1.setId(1L);
        user1.setEmail("test@example.com");

        User user2 = new User();
        user2.setId(2L);
        user2.setEmail("test@example.com");

        assertNotEquals(user1, user2);
    }

    @Test
    void equals_ShouldReturnFalseForDifferentEmail() {
        User user1 = new User();
        user1.setId(1L);
        user1.setEmail("test1@example.com");

        User user2 = new User();
        user2.setId(1L);
        user2.setEmail("test2@example.com");

        assertNotEquals(user1, user2);
    }

    @Test
    void equals_ShouldReturnFalseForNull() {
        User user = new User();
        user.setId(1L);

        assertNotEquals(null, user);
    }

    @Test
    void equals_ShouldReturnFalseForDifferentClass() {
        User user = new User();
        user.setId(1L);

        assertNotEquals("not a user", user);
    }

    @Test
    void toString_ShouldContainAllFields() {
        User user = new User();
        user.setId(1L);
        user.setName("John");
        user.setEmail("john@example.com");

        String toString = user.toString();

        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("name=John"));
        assertTrue(toString.contains("email=john@example.com"));
    }
}