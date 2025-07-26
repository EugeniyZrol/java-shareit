package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.groups.Default;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequest {

    public interface Create extends Default {}

    public interface Update extends Default {}

    @NotBlank(message = "Название должно быть указано", groups = Create.class)
    private String name;

    @NotBlank(message = "Описание должно быть указано", groups = Create.class)
    private String description;

    @NotNull(message = "Статус доступности должен быть указан", groups = Create.class)
    private Boolean available;

    private Long request;
}