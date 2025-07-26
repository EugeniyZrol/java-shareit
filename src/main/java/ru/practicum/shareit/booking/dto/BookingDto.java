package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.validated.EndDateAfterStartDate;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EndDateAfterStartDate
public class BookingDto {
    @NotNull(message = "Идентификатор не может быть пустым")
    private Long id;

    @NotNull(message = "Дата начала аренды должна быть указана")
    private LocalDateTime start;

    @NotNull(message = "Дата окончания аренды должна быть указана")
    private LocalDateTime end;

    private Long item;

    private Long booker;

    private Status status;
}
