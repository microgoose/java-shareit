package ru.practicum.shareit.features.user.exception;

import org.springframework.http.HttpStatus;
import ru.practicum.shareit.common.exceptions.ValidationException;

public class UserNotFound extends ValidationException {
    public Long userId;

    public UserNotFound(Long id) {
        super("User with id: " + id + " not found", HttpStatus.NOT_FOUND);
        this.userId = id;
    }
}
