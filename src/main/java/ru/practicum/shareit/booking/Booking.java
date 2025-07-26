package ru.practicum.shareit.booking;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
public class Booking {

    private Long id; //Уникальный идентификатор бронирования

    private LocalDateTime start; //Время начала бронирования

    private LocalDateTime end; // Время окончания бронирования

    private Long item; //идентификатор предмета бронирования

    private Long booker; //идентификатор пользователя, который бронирует

    private Status status; //статус бронирования
}
