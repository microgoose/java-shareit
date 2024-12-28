package ru.practicum.shareit.features.item.exception;

import org.springframework.http.HttpStatus;
import ru.practicum.shareit.common.exceptions.ValidationException;

public class OwnerNotFound extends ValidationException {
    public OwnerNotFound(Long id) {
        super("Владелец вещи с id: " + id + " не найденн", HttpStatus.NOT_FOUND);
    }
}
