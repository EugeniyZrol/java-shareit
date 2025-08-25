package ru.practicum.shareit.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class UserRequestTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldSerializeUserRequest() throws JsonProcessingException {

        UserRequest request = new UserRequest();
        request.setName("Test User");
        request.setEmail("test@email.com");

        String json = objectMapper.writeValueAsString(request);

        assertThat(json).contains("\"name\":\"Test User\"");
        assertThat(json).contains("\"email\":\"test@email.com\"");
    }

    @Test
    void shouldDeserializeUserRequest() throws JsonProcessingException {

        String json = "{\"name\":\"Test User\",\"email\":\"test@email.com\"}";

        UserRequest request = objectMapper.readValue(json, UserRequest.class);

        assertThat(request.getName()).isEqualTo("Test User");
        assertThat(request.getEmail()).isEqualTo("test@email.com");
    }

    @Test
    void shouldHandlePartialData() throws JsonProcessingException {

        String json = "{\"name\":\"Test User\"}";

        UserRequest request = objectMapper.readValue(json, UserRequest.class);

        assertThat(request.getName()).isEqualTo("Test User");
        assertThat(request.getEmail()).isNull();
    }

    @Test
    void shouldHandleEmptyObject() throws JsonProcessingException {

        String json = "{}";

        UserRequest request = objectMapper.readValue(json, UserRequest.class);

        assertThat(request.getName()).isNull();
        assertThat(request.getEmail()).isNull();
    }
}