package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RequestTest {

    @Test
    void request_ShouldWorkCorrectly() {
        User requestor = new User();
        requestor.setId(1L);

        Item item = new Item();
        item.setId(2L);

        LocalDateTime created = LocalDateTime.now();

        Request request = new Request();
        request.setId(1L);
        request.setDescription("Need a drill");
        request.setRequestor(requestor);
        request.setCreated(created);
        request.setItems(List.of(item));

        assertEquals(1L, request.getId());
        assertEquals("Need a drill", request.getDescription());
        assertEquals(requestor, request.getRequestor());
        assertEquals(created, request.getCreated());
        assertNotNull(request.getItems());
        assertEquals(1, request.getItems().size());
    }

    @Test
    void request_ShouldHaveDefaultCreatedTime() {
        Request request = new Request();

        request.setDescription("Test");

        assertNotNull(request.getCreated());
    }

    @Test
    void equals_ShouldReturnTrueForSameId() {
        Request request1 = new Request();
        request1.setId(1L);

        Request request2 = new Request();
        request2.setId(1L);

        assertEquals(request1, request2);
        assertEquals(request1.hashCode(), request2.hashCode());
    }

    @Test
    void equals_ShouldReturnFalseForDifferentId() {
        Request request1 = new Request();
        request1.setId(1L);

        Request request2 = new Request();
        request2.setId(2L);

        assertNotEquals(request1, request2);
    }

    @Test
    void equals_ShouldReturnFalseForNull() {
        Request request = new Request();
        request.setId(1L);

        assertNotEquals(null, request);
    }

    @Test
    void equals_ShouldReturnFalseForDifferentClass() {
        Request request = new Request();
        request.setId(1L);

        assertNotEquals("not a request", request);
    }

    @Test
    void toString_ShouldNotContainLazyFields() {
        Request request = new Request();
        request.setId(1L);
        request.setDescription("Test");

        String toString = request.toString();

        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("description='Test'"));
        assertFalse(toString.contains("requestor=")); // LAZY field should not be in toString
        assertFalse(toString.contains("items=")); // LAZY field should not be in toString
    }
}