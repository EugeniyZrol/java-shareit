package ru.practicum.shareit.item;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemRequestTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldSerializeItemRequest() throws JsonProcessingException {

        ItemRequest request = new ItemRequest("Test Item", "Test Description", true, 1L);

        String json = objectMapper.writeValueAsString(request);

        assertThat(json).contains("\"name\":\"Test Item\"");
        assertThat(json).contains("\"description\":\"Test Description\"");
        assertThat(json).contains("\"available\":true");
        assertThat(json).contains("\"requestId\":1");
    }

    @Test
    void shouldDeserializeItemRequest() throws JsonProcessingException {

        String json = "{\"name\":\"Test Item\",\"description\":\"Test Description\",\"available\":true,\"requestId\":1}";

        ItemRequest request = objectMapper.readValue(json, ItemRequest.class);

        assertThat(request.getName()).isEqualTo("Test Item");
        assertThat(request.getDescription()).isEqualTo("Test Description");
        assertThat(request.getAvailable()).isTrue();
        assertThat(request.getRequestId()).isEqualTo(1L);
    }

    @Test
    void shouldHandlePartialData() throws JsonProcessingException {

        String json = "{\"name\":\"Test Item\",\"available\":true}";

        ItemRequest request = objectMapper.readValue(json, ItemRequest.class);

        assertThat(request.getName()).isEqualTo("Test Item");
        assertThat(request.getAvailable()).isTrue();
        assertThat(request.getDescription()).isNull();
        assertThat(request.getRequestId()).isNull();
    }
}