package ru.practicum.shareit.features.item.mapper;

import ru.practicum.shareit.features.item.dto.*;
import ru.practicum.shareit.features.item.model.Item;
import ru.practicum.shareit.features.request.model.ItemRequest;
import ru.practicum.shareit.features.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

public class ItemMapper {
    public static BookingItemDto toItemDto(Item item, LocalDateTime lastBooking, LocalDateTime nextBooking, List<CommentDto> comments) {
        return new BookingItemDto(
            item.getId(),
            item.getName(),
            item.getDescription(),
            item.isAvailable(),
            item.getRequest() != null ? item.getRequest().getId() : null,
            lastBooking,
            nextBooking,
            comments
        );
    }

    public static ItemDto toItemDto(Item item) {
        return new ItemDto(
            item.getId(),
            item.getName(),
            item.getDescription(),
            item.isAvailable(),
            item.getRequest() != null ? item.getRequest().getId() : null
        );
    }

    public static Item toItem(CreateItemDto createItemDto, User owner) {
        Item item = new Item();
        item.setName(createItemDto.getName());
        item.setDescription(createItemDto.getDescription());
        item.setAvailable(createItemDto.getAvailable());
        item.setOwner(owner);

        if (createItemDto.getRequestId() == null) {
            item.setRequest(null);
        } else {
            item.setRequest(new ItemRequest(createItemDto.getRequestId()));
        }

        return item;
    }

    public static Item updateItem(Item item, UpdateItemDto updateItemDto) {
        if (updateItemDto.getName() != null) {
            item.setName(updateItemDto.getName());
        }
        if (updateItemDto.getDescription() != null) {
            item.setDescription(updateItemDto.getDescription());
        }
        if (updateItemDto.getAvailable() != null) {
            item.setAvailable(updateItemDto.getAvailable());
        }
        return item;
    }
}
