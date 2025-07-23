package ru.practicum.shareit.user.dto;

import lombok.Data;
import jakarta.validation.constraints.Email;

@Data
public class UserUpdateRequest {
    private String name;

    @Email(message = "Email должен быть валидным")
    private String email;
}