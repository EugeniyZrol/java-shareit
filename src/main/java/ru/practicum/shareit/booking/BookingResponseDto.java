package ru.practicum.shareit.booking;

import lombok.Data;
import ru.practicum.shareit.item.ItemResponse;
import ru.practicum.shareit.user.UserResponse;

import java.time.LocalDateTime;

@Data
public class BookingResponseDto {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private BookingStatus status;
    private ItemResponse item;
    private UserResponse booker;
}