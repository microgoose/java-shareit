package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateBookingDto {
    @NotNull(message = "Start date cannot be null")
    @FutureOrPresent(message = "Start date must be in the future or present")
    private LocalDateTime start;

    @NotNull(message = "End date cannot be null")
    @FutureOrPresent(message = "End date must be in the future or present")
    private LocalDateTime end;

    @NotNull(message = "Item ID cannot be null")
    private Long itemId;

    private String status;
}