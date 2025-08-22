package ru.practicum.shareit.booking;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import ru.practicum.shareit.item.ItemResponse;
import ru.practicum.shareit.user.UserResponse;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class BookingResponseDtoTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldSerializeBookingResponseDto() throws JsonProcessingException {

        ItemResponse item = new ItemResponse();
        item.setId(1L);
        item.setName("Test Item");

        UserResponse booker = new UserResponse();
        booker.setId(1L);

        BookingResponseDto dto = new BookingResponseDto();
        dto.setId(1L);
        dto.setStart(LocalDateTime.of(2024, 1, 1, 12, 0));
        dto.setEnd(LocalDateTime.of(2024, 1, 2, 12, 0));
        dto.setStatus(BookingStatus.APPROVED);
        dto.setItem(item);
        dto.setBooker(booker);

        String json = objectMapper.writeValueAsString(dto);

        assertThat(json).contains("\"id\":1");
        assertThat(json).contains("\"status\":\"APPROVED\"");
        assertThat(json).contains("\"start\":\"2024-01-01T12:00:00\"");
        assertThat(json).contains("\"name\":\"Test Item\"");
    }

    @Test
    void shouldDeserializeBookingResponseDto() throws JsonProcessingException {

        String json = "{\"id\":1,\"start\":\"2024-01-01T12:00:00\",\"end\":\"2024-01-02T12:00:00\",\"status\":\"APPROVED\",\"item\":{\"id\":1,\"name\":\"Test Item\"},\"booker\":{\"id\":1}}";

        BookingResponseDto dto = objectMapper.readValue(json, BookingResponseDto.class);

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getStatus()).isEqualTo(BookingStatus.APPROVED);
        assertThat(dto.getStart()).isEqualTo(LocalDateTime.of(2024, 1, 1, 12, 0));
        assertThat(dto.getItem()).isNotNull();
        assertThat(dto.getItem().getId()).isEqualTo(1L);
        assertThat(dto.getBooker()).isNotNull();
        assertThat(dto.getBooker().getId()).isEqualTo(1L);
    }
}