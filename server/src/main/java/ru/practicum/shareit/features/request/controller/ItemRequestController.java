package ru.practicum.shareit.features.request.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.config.HTTPHeadersConfig;
import ru.practicum.shareit.features.request.dto.ItemRequestDto;
import ru.practicum.shareit.features.request.service.ItemRequestService;

import java.util.List;

/**
 * Controller for handling item requests.
 */
@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    @Autowired
    public ItemRequestController(ItemRequestService itemRequestService) {
        this.itemRequestService = itemRequestService;
    }

    @PostMapping
    public ResponseEntity<ItemRequestDto> createRequest(
            @RequestHeader(HTTPHeadersConfig.X_SHARER_USER_ID) Long userId,
            @RequestBody ItemRequestDto itemRequestDto) {
        ItemRequestDto createdRequest = itemRequestService.createRequest(itemRequestDto, userId);
        return ResponseEntity.ok(createdRequest);
    }

    @GetMapping
    public ResponseEntity<List<ItemRequestDto>> getUserRequests(
            @RequestHeader(HTTPHeadersConfig.X_SHARER_USER_ID) Long userId) {
        List<ItemRequestDto> userRequests = itemRequestService.getUserRequests(userId);
        return ResponseEntity.ok(userRequests);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ItemRequestDto>> getAllRequests(
            @RequestHeader(HTTPHeadersConfig.X_SHARER_USER_ID) Long userId) {
        List<ItemRequestDto> allRequests = itemRequestService.getAllRequests(userId);
        return ResponseEntity.ok(allRequests);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<ItemRequestDto> getRequestById(@PathVariable Long requestId) {
        ItemRequestDto request = itemRequestService.getRequestById(requestId);
        return ResponseEntity.ok(request);
    }
}
