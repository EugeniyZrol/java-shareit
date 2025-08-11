package ru.practicum.shareit.booking;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.groups.Default;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookingRequestDto {

    public interface Create extends Default {}

    @NotNull(message = "ID вещи должен быть указан", groups = Create.class)
    private Long itemId;

    @FutureOrPresent(message = "Дата начала должна быть в настоящем или будущем", groups = Create.class)
    @NotNull(message = "Дата начала должна быть указана", groups = Create.class)
    private LocalDateTime start;

    @Future(message = "Дата окончания должна быть в будущем", groups = Create.class)
    @NotNull(message = "Дата окончания должна быть указана", groups = Create.class)
    private LocalDateTime end;
}