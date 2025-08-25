package ru.practicum.shareit.user;

import jakarta.validation.constraints.Email;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class UserUpdateRequest {
    @Length(min = 1, message = "Имя не может быть пустым")
    private String name;

    @Email(message = "Email должен быть валидным")
    private String email;
}