package ru.practicum.shareit.features.item.service;

import ru.practicum.shareit.features.item.dto.*;

import java.util.List;

public interface ItemService {
    ItemDto createItem(CreateItemDto createItemDto, Long userId);

    ItemDto updateItem(Long itemId, UpdateItemDto updateItemDto, Long userId);

    BookingItemDto getItemById(Long itemId, Long userId);

    List<ItemDto> getItemsByOwner(Long ownerId);

    List<ItemDto> searchItems(String text);

    CommentDto addComment(Long itemId, Long userId, CommentDto commentDto);
}
