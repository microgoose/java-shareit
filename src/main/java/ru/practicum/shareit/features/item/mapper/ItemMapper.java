package ru.practicum.shareit.features.item.mapper;

import ru.practicum.shareit.features.item.dto.CreateItemDto;
import ru.practicum.shareit.features.item.dto.ItemDto;
import ru.practicum.shareit.features.item.dto.UpdateItemDto;
import ru.practicum.shareit.features.item.model.Item;
import ru.practicum.shareit.features.request.model.ItemRequest;
import ru.practicum.shareit.features.user.model.User;

public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        return new ItemDto(
            item.getId(),
            item.getName(),
            item.getDescription(),
            item.isAvailable(),
            item.getRequest() != null ? item.getRequest().getId() : null
        );
    }

    public static Item toItem(ItemDto itemDto, ItemRequest request) {
        Item item = new Item();
        item.setId(itemDto.getId());
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.isAvailable());
        item.setRequest(request);
        return item;
    }

    public static Item toItem(CreateItemDto createItemDto, User owner) {
        Item item = new Item();
        item.setName(createItemDto.getName());
        item.setDescription(createItemDto.getDescription());
        item.setAvailable(createItemDto.getAvailable());
        item.setOwner(owner);
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
