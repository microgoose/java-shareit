package ru.practicum.shareit.features.booking.exception;

import org.springframework.http.HttpStatus;
import ru.practicum.shareit.common.exceptions.ValidationException;

public class BookingNotFound extends ValidationException {
    public BookingNotFound(Long id) {
        super("Бронь с id: " + id + " не найденна", HttpStatus.NOT_FOUND);
    }
}
