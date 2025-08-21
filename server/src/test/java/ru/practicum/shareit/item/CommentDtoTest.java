package ru.practicum.shareit.item;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.comment.CommentDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class CommentDtoTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JacksonTester<CommentDto> json;

    @Test
    void shouldSerializeCommentDto() throws Exception {

        LocalDateTime now = LocalDateTime.now().withNano(0);
        CommentDto commentDto = new CommentDto();
        commentDto.setId(1L);
        commentDto.setText("Great item!");
        commentDto.setAuthorName("Test User");
        commentDto.setCreated(now);

        JsonContent<CommentDto> result = json.write(commentDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.text").isEqualTo("Great item!");
        assertThat(result).extractingJsonPathStringValue("$.authorName").isEqualTo("Test User");
        assertThat(result).extractingJsonPathStringValue("$.created").isNotEmpty();
    }

    @Test
    void shouldDeserializeCommentDto() throws JsonProcessingException {

        String json = "{\"id\":1,\"text\":\"Great item!\",\"authorName\":\"Test User\",\"created\":\"2024-01-01T12:00:00\"}";

        CommentDto commentDto = objectMapper.readValue(json, CommentDto.class);

        assertThat(commentDto.getId()).isEqualTo(1L);
        assertThat(commentDto.getText()).isEqualTo("Great item!");
        assertThat(commentDto.getAuthorName()).isEqualTo("Test User");
        assertThat(commentDto.getCreated()).isEqualTo(LocalDateTime.of(2024, 1, 1, 12, 0));
    }

    @Test
    void shouldHandleNullValues() throws JsonProcessingException {

        CommentDto commentDto = new CommentDto();
        commentDto.setText("Test");

        String json = objectMapper.writeValueAsString(commentDto);
        CommentDto result = objectMapper.readValue(json, CommentDto.class);

        assertThat(result.getId()).isNull();
        assertThat(result.getText()).isEqualTo("Test");
        assertThat(result.getAuthorName()).isNull();
        assertThat(result.getCreated()).isNull();
    }
}