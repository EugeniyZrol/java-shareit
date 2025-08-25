package ru.practicum.shareit.itemRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemResponseDto {
    private Long id;
    private String name;
    private Long ownerId;
    private String description;
    private Boolean available;
    private Long requestId;
}