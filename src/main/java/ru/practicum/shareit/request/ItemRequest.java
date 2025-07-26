package ru.practicum.shareit.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
public class ItemRequest {

    private Long id; // Уникальный идентификатор запроса

    @NotBlank(message = "описание не может быть пустым")
    private String description; // Текст запроса содержащий описание

    @NotNull(message = "Пользователь должен быть указан")
    private Long requestor; //Пользователь создавший запрос

    private LocalDateTime created; //Дата и время создания

    public ItemRequest(ItemRequest other) {
        if (other != null) {
            this.id = other.id;
            this.description = other.description;
            this.requestor = other.requestor;
            this.created = other.created;
        }
    }
}
