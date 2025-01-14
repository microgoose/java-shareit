package ru.practicum.shareit.features.item.controller;

import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.features.item.dto.*;
import ru.practicum.shareit.features.item.service.ItemService;

import java.util.List;

import static ru.practicum.shareit.config.HTTPHeadersConfig.X_SHARER_USER_ID;

@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public ItemDto addItem(@RequestBody CreateItemDto createItemDto,
                           @RequestHeader(X_SHARER_USER_ID) Long userId) {
        return itemService.createItem(createItemDto, userId);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(
            @PathVariable Long itemId,
            @RequestBody CommentDto commentDto,
            @RequestHeader(X_SHARER_USER_ID) Long userId) {
        return itemService.addComment(itemId, userId, commentDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@PathVariable Long itemId,
                              @RequestBody UpdateItemDto updateItemDto,
                              @RequestHeader(X_SHARER_USER_ID) Long userId) {
        return itemService.updateItem(itemId, updateItemDto, userId);
    }

    @GetMapping("/{itemId}")
    public BookingItemDto getItem(@PathVariable Long itemId,
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
