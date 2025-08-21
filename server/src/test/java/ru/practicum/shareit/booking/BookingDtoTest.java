package ru.practicum.shareit.booking;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class BookingDtoTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldSerializeBookingRequestDto() throws JsonProcessingException {

        BookingRequestDto dto = new BookingRequestDto();
        dto.setItemId(1L);
        dto.setStart(LocalDateTime.of(2024, 1, 1, 12, 0));
        dto.setEnd(LocalDateTime.of(2024, 1, 2, 12, 0));

        String json = objectMapper.writeValueAsString(dto);

        assertThat(json).contains("\"itemId\":1");
        assertThat(json).contains("\"start\":\"2024-01-01T12:00:00\"");
        assertThat(json).contains("\"end\":\"2024-01-02T12:00:00\"");
    }

    @Test
    void shouldDeserializeBookingRequestDto() throws JsonProcessingException {

        String json = "{\"itemId\":1,\"start\":\"2024-01-01T12:00:00\",\"end\":\"2024-01-02T12:00:00\"}";

        BookingRequestDto dto = objectMapper.readValue(json, BookingRequestDto.class);

        assertThat(dto.getItemId()).isEqualTo(1L);
        assertThat(dto.getStart()).isEqualTo(LocalDateTime.of(2024, 1, 1, 12, 0));
        assertThat(dto.getEnd()).isEqualTo(LocalDateTime.of(2024, 1, 2, 12, 0));
    }

    @Test
    void shouldHandleMissingFields() throws JsonProcessingException {

        String json = "{\"itemId\":1}";

        BookingRequestDto dto = objectMapper.readValue(json, BookingRequestDto.class);

        assertThat(dto.getItemId()).isEqualTo(1L);
        assertThat(dto.getStart()).isNull();
        assertThat(dto.getEnd()).isNull();
    }
}