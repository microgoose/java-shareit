package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.config.HTTPHeadersConfig;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CreateItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;

@Controller
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {

    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> addItem(@RequestBody @Valid CreateItemDto createItemDto,
                                          @RequestHeader(HTTPHeadersConfig.X_SHARER_USER_ID) Long userId) {
        log.info("Adding item {}, userId={}", createItemDto, userId);
        return itemClient.addItem(userId, createItemDto);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@PathVariable @Positive Long itemId,
                                             @RequestBody @Valid CommentDto commentDto,
                                             @RequestHeader(HTTPHeadersConfig.X_SHARER_USER_ID) Long userId) {
        log.info("Adding comment to item {}, userId={}, comment={}", itemId, userId, commentDto);
        return itemClient.addComment(userId, itemId, commentDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@PathVariable @Positive Long itemId,
                                             @RequestBody @Valid UpdateItemDto updateItemDto,
                                             @RequestHeader(HTTPHeadersConfig.X_SHARER_USER_ID) Long userId) {
        log.info("Updating item {}, userId={}, updateDto={}", itemId, userId, updateItemDto);
        return itemClient.updateItem(userId, itemId, updateItemDto);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItem(@PathVariable @Positive Long itemId,
                                          @RequestHeader(HTTPHeadersConfig.X_SHARER_USER_ID) Long userId) {
        log.info("Getting item {}, userId={}", itemId, userId);
        return itemClient.getItem(userId, itemId);
    }

    @GetMapping
    public ResponseEntity<Object> getItemsByOwner(@RequestHeader(HTTPHeadersConfig.X_SHARER_USER_ID) Long userId) {
        log.info("Getting items for owner with userId={}", userId);
        return itemClient.getItemsByOwner(userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItems(@RequestParam("text") @NotBlank String text,
                                              @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                              @RequestParam(name = "size", defaultValue = "10") @Positive Integer size) {
        log.info("Searching items with text='{}', from={}, size={}", text, from, size);
        return itemClient.searchItems(text, from, size);
    }
}
