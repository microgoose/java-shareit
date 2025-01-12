package ru.practicum.shareit.common.exceptions;

import org.springframework.http.HttpStatus;

public class ValidationException extends RuntimeException {
    public final HttpStatus httpStatus;

    public ValidationException(String message) {
        super(message);
        this.httpStatus = HttpStatus.BAD_REQUEST;
    }

    public ValidationException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
