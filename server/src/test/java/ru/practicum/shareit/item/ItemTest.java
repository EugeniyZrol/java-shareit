package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.request.Request;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ItemTest {

    @Test
    void item_ShouldWorkCorrectly() {
        Request request = new Request();
        request.setId(1L);

        Comment comment = new Comment();
        comment.setId(1L);

        Item item = new Item();
        item.setId(1L);
        item.setName("Test Item");
        item.setDescription("Test Description");
        item.setAvailable(true);
        item.setOwnerId(2L);
        item.setRequest(request);
        item.setComments(List.of(comment));

        assertEquals(1L, item.getId());
        assertEquals("Test Item", item.getName());
        assertEquals("Test Description", item.getDescription());
        assertTrue(item.getAvailable());
        assertEquals(2L, item.getOwnerId());
        assertEquals(request, item.getRequest());
        assertNotNull(item.getComments());
        assertEquals(1, item.getComments().size());
    }

    @Test
    void equals_ShouldReturnTrueForSameIdNameOwner() {
        Item item1 = new Item();
        item1.setId(1L);
        item1.setName("Item");
        item1.setOwnerId(2L);

        Item item2 = new Item();
        item2.setId(1L);
        item2.setName("Item");
        item2.setOwnerId(2L);

        assertEquals(item1, item2);
        assertEquals(item1.hashCode(), item2.hashCode());
    }

    @Test
    void equals_ShouldReturnFalseForDifferentId() {
        Item item1 = new Item();
        item1.setId(1L);
        item1.setName("Item");
        item1.setOwnerId(2L);

        Item item2 = new Item();
        item2.setId(2L);
        item2.setName("Item");
        item2.setOwnerId(2L);

        assertNotEquals(item1, item2);
    }

    @Test
    void equals_ShouldReturnFalseForDifferentName() {
        Item item1 = new Item();
        item1.setId(1L);
        item1.setName("Item1");
        item1.setOwnerId(2L);

        Item item2 = new Item();
        item2.setId(1L);
        item2.setName("Item2");
        item2.setOwnerId(2L);

        assertNotEquals(item1, item2);
    }

    @Test
    void equals_ShouldReturnFalseForDifferentOwner() {
        Item item1 = new Item();
        item1.setId(1L);
        item1.setName("Item");
        item1.setOwnerId(2L);

        Item item2 = new Item();
        item2.setId(1L);
        item2.setName("Item");
        item2.setOwnerId(3L);

        assertNotEquals(item1, item2);
    }

    @Test
    void equals_ShouldReturnFalseForNull() {
        Item item = new Item();
        item.setId(1L);

        assertNotEquals(null, item);
    }

    @Test
    void equals_ShouldReturnFalseForDifferentClass() {
        Item item = new Item();
        item.setId(1L);

        assertNotEquals("not an item", item);
    }
}