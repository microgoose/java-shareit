package ru.practicum.shareit.features.booking.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateBookingDto {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private Long itemId;
    private String status;
}