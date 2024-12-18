package ru.practicum.shareit.features.item.service;

import ru.practicum.shareit.features.item.dto.CreateItemDto;
import ru.practicum.shareit.features.item.dto.ItemDto;
import ru.practicum.shareit.features.item.dto.UpdateItemDto;

import java.util.List;

public interface ItemService {
    ItemDto createItem(CreateItemDto createItemDto, Long userId);

    ItemDto updateItem(Long itemId, UpdateItemDto updateItemDto, Long userId);

    ItemDto getItemById(Long itemId, Long userId);

    List<ItemDto> getItemsByOwner(Long ownerId);

    List<ItemDto> searchItems(String text);
}
