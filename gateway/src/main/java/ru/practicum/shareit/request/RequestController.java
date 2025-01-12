package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.config.HTTPHeadersConfig;
import ru.practicum.shareit.request.dto.ItemRequestDto;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class RequestController {

    private final RequestClient requestClient;

    @PostMapping
    public ResponseEntity<Object> createRequest(@RequestBody @Valid ItemRequestDto itemRequestDto,
                                                @RequestHeader(HTTPHeadersConfig.X_SHARER_USER_ID) Long userId) {
        log.info("Creating request {}, userId={}", itemRequestDto, userId);
        return requestClient.createRequest(userId, itemRequestDto);
    }

    @GetMapping
    public ResponseEntity<Object> getUserRequests(@RequestHeader(HTTPHeadersConfig.X_SHARER_USER_ID) Long userId) {
        log.info("Getting user requests for userId={}", userId);
        return requestClient.getUserRequests(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllRequests(@RequestHeader(HTTPHeadersConfig.X_SHARER_USER_ID) Long userId) {
        log.info("Getting all requests for userId={}", userId);
        return requestClient.getAllRequests(userId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequestById(@PathVariable Long requestId,
                                                 @RequestHeader(HTTPHeadersConfig.X_SHARER_USER_ID) Long userId) {
        log.info("Getting request with requestId={}, userId={}", requestId, userId);
        return requestClient.getRequestById(userId, requestId);
    }
}

