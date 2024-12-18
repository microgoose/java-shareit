package ru.practicum.shareit.features.item.exception;

import org.springframework.http.HttpStatus;
import ru.practicum.shareit.common.exceptions.ValidationException;

public class ItemNotFound extends ValidationException {
    public ItemNotFound(Long id) {
        super("Item with id: " + id + " not found", HttpStatus.NOT_FOUND);
    }
}
