package ru.practicum.shareit.features.booking.mapper;

import ru.practicum.shareit.features.booking.dto.BookingDto;
import ru.practicum.shareit.features.booking.model.Booking;
import ru.practicum.shareit.features.item.mapper.ItemMapper;
import ru.practicum.shareit.features.user.mapper.UserMapper;

public class BookingMapper {
    public static BookingDto mapToDto(Booking booking) {
        BookingDto dto = new BookingDto();
        dto.setId(booking.getId());
        dto.setStart(booking.getStart());
        dto.setEnd(booking.getEnd());
        dto.setItem(ItemMapper.toItemDto(booking.getItem()));
        dto.setBooker(UserMapper.toUserDto(booking.getBooker()));
        dto.setStatus(booking.getStatus().name());
        return dto;
    }
}
