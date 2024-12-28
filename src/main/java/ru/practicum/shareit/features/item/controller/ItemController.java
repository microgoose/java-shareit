package ru.practicum.shareit.features.item.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.features.item.dto.CreateItemDto;
import ru.practicum.shareit.features.item.dto.ItemDto;
import ru.practicum.shareit.features.item.dto.UpdateItemDto;
import ru.practicum.shareit.features.item.service.ItemService;

import java.util.List;

import static ru.practicum.shareit.features.item.config.UserRequestConfig.X_SHARER_USER_ID;

@RestController
@RequestMapping("/items")
@Valid
public class ItemController {
    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public ItemDto addItem(@RequestBody @Valid CreateItemDto createItemDto,
                           @RequestHeader(X_SHARER_USER_ID) Long userId) {
        return itemService.createItem(createItemDto, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@PathVariable Long itemId,
                              @RequestBody @Valid UpdateItemDto updateItemDto,
                              @RequestHeader(X_SHARER_USER_ID) Long userId) {
        return itemService.updateItem(itemId, updateItemDto, userId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItem(@PathVariable Long itemId,
                           @RequestHeader(X_SHARER_USER_ID) Long userId) {
        return itemService.getItemById(itemId, userId);
    }

    @GetMapping
    public List<ItemDto> getItemsByOwner(@RequestHeader(X_SHARER_USER_ID) Long userId) {
        return itemService.getItemsByOwner(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestParam("text") String text) {
        return itemService.searchItems(text);
    }
}
