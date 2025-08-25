package ru.practicum.shareit.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserRequest {
    @NotBlank(message = "Имя не может быть пустым")
    private String name;

    @Email(message = "Email должен быть валидным")
    @NotBlank(message = "Email не может быть пустым")
    private String email;
}