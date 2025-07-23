package ru.practicum.shareit.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
public class User {
    @NotNull(message = "Id должен быть указан")
    private Long id; //Уникальный идентификатор

    @NotBlank(message = "Имя не может быть пустым")
    private String name; // Имя или логин пользователя

    @Email(message = "Имейл должен содержать @")
    @NotBlank(message = "Имейл должен быть указан")
    private String email; //Имейл пользователя
}