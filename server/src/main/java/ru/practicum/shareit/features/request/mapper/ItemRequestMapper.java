package ru.practicum.shareit.features.request.mapper;

import ru.practicum.shareit.features.item.model.Item;
import ru.practicum.shareit.features.request.dto.ItemRequestDto;
import ru.practicum.shareit.features.request.dto.ItemResponseDto;
import ru.practicum.shareit.features.request.model.ItemRequest;
import ru.practicum.shareit.features.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

public class ItemRequestMapper {
    public static ItemResponseDto mapToResponseDto(Item item) {
        return ItemResponseDto.builder()
                .id(item.getId())
                .name(item.getName())
                .ownerId(item.getOwner().getId())
                .build();
    }

    public static ItemRequest mapToEntity(ItemRequestDto itemRequestDto, Long userId) {
        User requestor = new User();
        requestor.setId(userId);

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription(itemRequestDto.getDescription());
        itemRequest.setRequestor(requestor);
        itemRequest.setCreated(itemRequestDto.getCreated());

        return itemRequest;
    }

    public static ItemRequestDto mapToDto(ItemRequest itemRequest) {
        List<ItemResponseDto> items = itemRequest.getResponses() == null ? null : itemRequest.getResponses().stream()
                .map(ItemRequestMapper::mapToResponseDto)
                .collect(Collectors.toList());

        return ItemRequestDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreated())
                .requestorId(itemRequest.getRequestor().getId())
                .items(items)
                .build();
    }
}
