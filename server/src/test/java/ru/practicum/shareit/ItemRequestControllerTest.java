package ru.practicum.shareit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import ru.practicum.shareit.features.request.controller.ItemRequestController;
import ru.practicum.shareit.features.request.dto.ItemRequestDto;
import ru.practicum.shareit.features.request.service.ItemRequestService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ItemRequestControllerTest {

    private ItemRequestService itemRequestService;
    private ItemRequestController itemRequestController;

    @BeforeEach
    void setUp() {
        itemRequestService = mock(ItemRequestService.class);
        itemRequestController = new ItemRequestController(itemRequestService);
    }

    @Test
    void createRequest_shouldReturnCreatedRequest_whenValidInput() {
        Long userId = 1L;
        ItemRequestDto requestDto = new ItemRequestDto(null, "Need a laptop", null, null, null);
        ItemRequestDto createdRequestDto = new ItemRequestDto(1L, "Need a laptop", null, null, null);

        when(itemRequestService.createRequest(requestDto, userId)).thenReturn(createdRequestDto);

        ResponseEntity<ItemRequestDto> response = itemRequestController.createRequest(userId, requestDto);

        assertEquals(ResponseEntity.ok(createdRequestDto), response);
        verify(itemRequestService, times(1)).createRequest(requestDto, userId);
    }

    @Test
    void getUserRequests_shouldReturnListOfRequests_whenRequestsExist() {
        Long userId = 1L;
        List<ItemRequestDto> expectedRequests = List.of(
                new ItemRequestDto(1L, "Need a laptop", null, null, null),
                new ItemRequestDto(2L, "Looking for a bike", null, null, null)
        );

        when(itemRequestService.getUserRequests(userId)).thenReturn(expectedRequests);

        ResponseEntity<List<ItemRequestDto>> response = itemRequestController.getUserRequests(userId);

        assertEquals(ResponseEntity.ok(expectedRequests), response);
        verify(itemRequestService, times(1)).getUserRequests(userId);
    }

    @Test
    void getAllRequests_shouldReturnListOfRequests_whenCalled() {
        Long userId = 1L;
        List<ItemRequestDto> expectedRequests = List.of(
                new ItemRequestDto(3L, "Need a tent", null, null, null),
                new ItemRequestDto(4L, "Looking for a drill", null, null, null)
        );

        when(itemRequestService.getAllRequests(userId)).thenReturn(expectedRequests);

        ResponseEntity<List<ItemRequestDto>> response = itemRequestController.getAllRequests(userId);

        assertEquals(ResponseEntity.ok(expectedRequests), response);
        verify(itemRequestService, times(1)).getAllRequests(userId);
    }

    @Test
    void getRequestById_shouldReturnRequest_whenRequestExists() {
        Long requestId = 1L;
        ItemRequestDto expectedRequest = new ItemRequestDto(requestId, "Need a bike", null, null, null);

        when(itemRequestService.getRequestById(requestId)).thenReturn(expectedRequest);

        ResponseEntity<ItemRequestDto> response = itemRequestController.getRequestById(requestId);

        assertEquals(ResponseEntity.ok(expectedRequest), response);
        verify(itemRequestService, times(1)).getRequestById(requestId);
    }
}
