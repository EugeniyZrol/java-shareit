package ru.practicum.shareit.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class UserResponseTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldSerializeUserResponse() throws JsonProcessingException {

        UserResponse response = new UserResponse();
        response.setId(1L);
        response.setName("Test User");
        response.setEmail("test@email.com");

        String json = objectMapper.writeValueAsString(response);

        assertThat(json).contains("\"id\":1");
        assertThat(json).contains("\"name\":\"Test User\"");
        assertThat(json).contains("\"email\":\"test@email.com\"");
    }

    @Test
    void shouldDeserializeUserResponse() throws JsonProcessingException {

        String json = "{\"id\":1,\"name\":\"Test User\",\"email\":\"test@email.com\"}";

        UserResponse response = objectMapper.readValue(json, UserResponse.class);

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getName()).isEqualTo("Test User");
        assertThat(response.getEmail()).isEqualTo("test@email.com");
    }

    @Test
    void shouldHandleMissingId() throws JsonProcessingException {

        String json = "{\"name\":\"Test User\",\"email\":\"test@email.com\"}";

        UserResponse response = objectMapper.readValue(json, UserResponse.class);

        assertThat(response.getId()).isNull();
        assertThat(response.getName()).isEqualTo("Test User");
        assertThat(response.getEmail()).isEqualTo("test@email.com");
    }
}