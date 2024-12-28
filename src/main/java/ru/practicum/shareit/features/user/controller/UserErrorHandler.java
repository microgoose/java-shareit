package ru.practicum.shareit.features.user.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.common.dto.ErrorResponse;
import ru.practicum.shareit.common.exceptions.ValidationException;
import ru.practicum.shareit.features.user.exception.UserNotFound;

@RestControllerAdvice(basePackages = "ru.practicum.shareit.features.user.controller")
@Slf4j
public class UserErrorHandler {
    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleValidationException(ValidationException ex) {
        return new ResponseEntity<>(
            new ErrorResponse("Ошибка валидации", ex.getMessage()),
            ex.httpStatus
        );
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleNotFoundException(UserNotFound ex) {
        return new ResponseEntity<>(
            new ErrorResponse("Не найденно", ex.getMessage()),
            ex.httpStatus
        );
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleInternalError(Exception ex) {
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(
            new ErrorResponse("Внутренняя ошибка", ex.getMessage()),
            HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
