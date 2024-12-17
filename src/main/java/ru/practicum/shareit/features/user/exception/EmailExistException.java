package ru.practicum.shareit.features.user.exception;

import org.springframework.http.HttpStatus;
import ru.practicum.shareit.common.exceptions.ValidationException;

public class EmailExistException extends ValidationException {
    public EmailExistException(String email) {
        super("Email already exists: " + email, HttpStatus.CONFLICT);
    }
}
