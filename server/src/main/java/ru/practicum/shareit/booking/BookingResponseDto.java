package ru.practicum.shareit.booking;

import lombok.Data;
import ru.practicum.shareit.item.ItemDtoResponse;
import ru.practicum.shareit.user.UserResponse;

import java.time.LocalDateTime;

@Data
public class BookingResponseDto {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private BookingStatus status;
    private ItemDtoResponse item;
    private UserResponse booker;
}