package ru.practicum.shareit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.practicum.shareit.request.RequestClient;
import ru.practicum.shareit.request.RequestController;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class RequestControllerTest {

    private RequestClient requestClient;
    private RequestController requestController;

    @BeforeEach
    void setUp() {
        requestClient = mock(RequestClient.class);
        requestController = new RequestController(requestClient);
    }

    @Test
    void createRequest_shouldReturnResponse_whenRequestIsCreated() {
        Long userId = 1L;
        ItemRequestDto itemRequestDto = new ItemRequestDto("Need a drill", LocalDateTime.now());

        ResponseEntity<Object> expectedResponse = ResponseEntity.status(HttpStatus.CREATED).body("Request created");
        when(requestClient.createRequest(userId, itemRequestDto)).thenReturn(expectedResponse);

        ResponseEntity<Object> response = requestController.createRequest(itemRequestDto, userId);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Request created", response.getBody());
        verify(requestClient, times(1)).createRequest(userId, itemRequestDto);
    }

    @Test
    void getUserRequests_shouldReturnResponse_whenRequestsExist() {
        Long userId = 1L;

        ResponseEntity<Object> expectedResponse = ResponseEntity.ok(List.of("Request1", "Request2"));
        when(requestClient.getUserRequests(userId)).thenReturn(expectedResponse);

        ResponseEntity<Object> response = requestController.getUserRequests(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(List.of("Request1", "Request2"), response.getBody());
        verify(requestClient, times(1)).getUserRequests(userId);
    }

    @Test
    void getAllRequests_shouldReturnResponse_whenRequestsExist() {
        Long userId = 1L;

        ResponseEntity<Object> expectedResponse = ResponseEntity.ok(List.of("Request1", "Request2", "Request3"));
        when(requestClient.getAllRequests(userId)).thenReturn(expectedResponse);

        ResponseEntity<Object> response = requestController.getAllRequests(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(List.of("Request1", "Request2", "Request3"), response.getBody());
        verify(requestClient, times(1)).getAllRequests(userId);
    }

    @Test
    void getRequestById_shouldReturnResponse_whenRequestExists() {
        Long userId = 1L;
        Long requestId = 1L;

        ResponseEntity<Object> expectedResponse = ResponseEntity.ok("Request details");
        when(requestClient.getRequestById(userId, requestId)).thenReturn(expectedResponse);

        ResponseEntity<Object> response = requestController.getRequestById(requestId, userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Request details", response.getBody());
        verify(requestClient, times(1)).getRequestById(userId, requestId);
    }
}
