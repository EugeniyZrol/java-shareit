package ru.practicum.shareit.item.comment;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CommentTest {

    @Test
    void comment_ShouldWorkCorrectly() {
        Item item = new Item();
        item.setId(1L);

        User author = new User();
        author.setId(2L);

        LocalDateTime created = LocalDateTime.now();

        Comment comment = new Comment();
        comment.setId(1L);
        comment.setText("Great comment!");
        comment.setItem(item);
        comment.setAuthor(author);
        comment.setCreated(created);

        assertEquals(1L, comment.getId());
        assertEquals("Great comment!", comment.getText());
        assertEquals(item, comment.getItem());
        assertEquals(author, comment.getAuthor());
        assertEquals(created, comment.getCreated());
    }

    @Test
    void comment_ShouldHaveDefaultCreatedTime() {
        Comment comment = new Comment();

        comment.setText("Test");

        assertNotNull(comment.getCreated());
    }

    @Test
    void toString_ShouldContainAllFields() {

        Comment comment = new Comment();
        comment.setId(1L);
        comment.setText("Test");

        String toString = comment.toString();

        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("text=Test"));
        assertTrue(toString.contains("item="));
        assertTrue(toString.contains("author="));
    }
}