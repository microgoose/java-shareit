package ru.practicum.shareit.features.item.exception;

import org.springframework.http.HttpStatus;
import ru.practicum.shareit.common.exceptions.ValidationException;

public class ItemNotFound extends ValidationException {
    public Long itemId;

    public ItemNotFound(Long id) {
        super("Вещь с id: " + id + " не найденна", HttpStatus.NOT_FOUND);
        this.itemId = id;
    }
}
