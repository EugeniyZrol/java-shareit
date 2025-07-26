package ru.practicum.shareit.user.dto;

import jakarta.validation.groups.Default;
import lombok.Data;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Data
public class UserRequest {

    public interface Create extends Default {}

    public interface Update extends Default {}

    @NotBlank(message = "Имя не может быть пустым", groups = Create.class)
    private String name;

    @Email(message = "Email должен быть валидным", groups = {Create.class, Update.class})
    @NotBlank(message = "Email не может быть пустым", groups = Create.class)
    private String email;
}