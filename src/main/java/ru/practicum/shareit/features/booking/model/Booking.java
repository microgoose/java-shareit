package ru.practicum.shareit.features.booking.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.features.booking.common.BookingStatus;
import ru.practicum.shareit.features.item.model.Item;
import ru.practicum.shareit.features.user.model.User;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class Booking {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private Item item;
    private User booker;
    private BookingStatus status;
}
