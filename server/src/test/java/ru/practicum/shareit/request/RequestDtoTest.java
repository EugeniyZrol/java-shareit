package ru.practicum.shareit.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class RequestDtoTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldSerializeRequestDto() throws JsonProcessingException {

        RequestDto.ItemResponseDto itemDto = RequestDto.ItemResponseDto.builder()
                .id(1L)
                .name("Дрель")
                .description("Мощная дрель")
                .available(true)
                .ownerId(2L)
                .requestId(1L)
                .build();

        RequestDto requestDto = RequestDto.builder()
                .id(1L)
                .description("Нужна дрель")
                .created(LocalDateTime.of(2024, 1, 1, 12, 0))
                .items(List.of(itemDto))
                .build();

        String json = objectMapper.writeValueAsString(requestDto);

        assertThat(json).contains("\"id\":1");
        assertThat(json).contains("\"description\":\"Нужна дрель\"");
        assertThat(json).contains("\"created\":\"2024-01-01T12:00:00\"");
        assertThat(json).contains("\"name\":\"Дрель\"");
    }

    @Test
    void shouldDeserializeRequestDto() throws JsonProcessingException {

        String json = "{\"id\":1,\"description\":\"Нужна дрель\",\"created\":\"2024-01-01T12:00:00\",\"items\":[{\"id\":1,\"name\":\"Дрель\",\"description\":\"Мощная дрель\",\"available\":true,\"ownerId\":2,\"requestId\":1}]}";

        RequestDto requestDto = objectMapper.readValue(json, RequestDto.class);

        assertThat(requestDto.getId()).isEqualTo(1L);
        assertThat(requestDto.getDescription()).isEqualTo("Нужна дрель");
        assertThat(requestDto.getCreated()).isEqualTo(LocalDateTime.of(2024, 1, 1, 12, 0));
        assertThat(requestDto.getItems()).hasSize(1);
        assertThat(requestDto.getItems().get(0).getName()).isEqualTo("Дрель");
    }

    @Test
    void shouldHandleRequestDtoWithoutItems() throws JsonProcessingException {

        String json = "{\"id\":1,\"description\":\"Нужна дрель\",\"created\":\"2024-01-01T12:00:00\"}";

        RequestDto requestDto = objectMapper.readValue(json, RequestDto.class);

        assertThat(requestDto.getId()).isEqualTo(1L);
        assertThat(requestDto.getDescription()).isEqualTo("Нужна дрель");
        assertThat(requestDto.getItems()).isNull();
    }

    @Test
    void shouldHandleItemResponseDto() throws JsonProcessingException {

        RequestDto.ItemResponseDto itemDto = RequestDto.ItemResponseDto.builder()
                .id(1L)
                .name("Дрель")
                .available(true)
                .build();

        String json = objectMapper.writeValueAsString(itemDto);

        assertThat(json).contains("\"id\":1");
        assertThat(json).contains("\"name\":\"Дрель\"");
        assertThat(json).contains("\"available\":true");
    }

    @Test
    void shouldDeserializeItemResponseDto() throws JsonProcessingException {

        String json = "{\"id\":1,\"name\":\"Дрель\",\"description\":\"Мощная дрель\",\"available\":true,\"ownerId\":2,\"requestId\":1}";

        RequestDto.ItemResponseDto itemDto = objectMapper.readValue(json, RequestDto.ItemResponseDto.class);

        assertThat(itemDto.getId()).isEqualTo(1L);
        assertThat(itemDto.getName()).isEqualTo("Дрель");
        assertThat(itemDto.getDescription()).isEqualTo("Мощная дрель");
        assertThat(itemDto.getAvailable()).isTrue();
        assertThat(itemDto.getOwnerId()).isEqualTo(2L);
        assertThat(itemDto.getRequestId()).isEqualTo(1L);
    }
}