package ru.practicum.shareit.features.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.common.exceptions.ValidationException;
import ru.practicum.shareit.features.item.dto.CreateItemDto;
import ru.practicum.shareit.features.item.dto.ItemDto;
import ru.practicum.shareit.features.item.dto.UpdateItemDto;
import ru.practicum.shareit.features.item.exception.ItemNotFound;
import ru.practicum.shareit.features.item.mapper.ItemMapper;
import ru.practicum.shareit.features.item.model.Item;
import ru.practicum.shareit.features.item.repository.ItemRepository;
import ru.practicum.shareit.features.user.exception.UserNotFound;
import ru.practicum.shareit.features.user.model.User;
import ru.practicum.shareit.features.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public ItemDto createItem(CreateItemDto createItemDto, Long userId) {
        User owner = getUserById(userId);
        Item item = ItemMapper.toItem(createItemDto, owner);

        if (createItemDto.getRequestId() != null) {
            item.setRequest(null);
        }

        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    public ItemDto updateItem(Long itemId, UpdateItemDto updateItemDto, Long userId) {
        Item item = getItemById(itemId);
        if (!item.getOwner().getId().equals(userId)) {
            throw new ValidationException("Редактировать вещь может только её владелец", HttpStatus.FORBIDDEN);
        }
        Item updatedItem = ItemMapper.updateItem(item, updateItemDto);
        return ItemMapper.toItemDto(itemRepository.save(updatedItem));
    }

    public ItemDto getItemById(Long itemId, Long userId) {
        Item item = getItemById(itemId);
        return ItemMapper.toItemDto(item);
    }

    public List<ItemDto> getItemsByOwner(Long ownerId) {
        getUserById(ownerId);
        return itemRepository.findByOwnerId(ownerId)
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    public List<ItemDto> searchItems(String text) {
        if (text == null || text.isBlank()) {
            return List.of();
        }
        return itemRepository.searchByText(text.toLowerCase())
                .stream()
                .filter(Item::isAvailable)
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFound(userId));
    }

    private Item getItemById(Long itemId) {
        return itemRepository.findById(itemId).orElseThrow(() -> new ItemNotFound(itemId));
    }
}
