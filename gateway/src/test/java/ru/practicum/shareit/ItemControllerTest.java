package ru.practicum.shareit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.practicum.shareit.item.ItemClient;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CreateItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ItemControllerTest {

    private ItemClient itemClient;
    private ItemController itemController;

    @BeforeEach
    void setUp() {
        itemClient = mock(ItemClient.class);
        itemController = new ItemController(itemClient);
    }

    @Test
    void addItem_shouldReturnResponse_whenItemIsAdded() {
        Long userId = 1L;
        CreateItemDto createItemDto = new CreateItemDto("Item1", "Description1", true, null);

        ResponseEntity<Object> expectedResponse = ResponseEntity.status(HttpStatus.CREATED).body("Item added");
        when(itemClient.addItem(userId, createItemDto)).thenReturn(expectedResponse);

        ResponseEntity<Object> response = itemController.addItem(createItemDto, userId);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Item added", response.getBody());
        verify(itemClient, times(1)).addItem(userId, createItemDto);
    }

    @Test
    void addComment_shouldReturnResponse_whenCommentIsAdded() {
        Long userId = 1L;
        Long itemId = 1L;
        CommentDto commentDto = new CommentDto("Great item!");

        ResponseEntity<Object> expectedResponse = ResponseEntity.status(HttpStatus.CREATED).body("Comment added");
        when(itemClient.addComment(userId, itemId, commentDto)).thenReturn(expectedResponse);

        ResponseEntity<Object> response = itemController.addComment(itemId, commentDto, userId);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Comment added", response.getBody());
        verify(itemClient, times(1)).addComment(userId, itemId, commentDto);
    }

    @Test
    void updateItem_shouldReturnResponse_whenItemIsUpdated() {
        Long userId = 1L;
        Long itemId = 1L;
        UpdateItemDto updateItemDto = new UpdateItemDto("Updated Item", "Updated Description", true);

        ResponseEntity<Object> expectedResponse = ResponseEntity.ok("Item updated");
        when(itemClient.updateItem(userId, itemId, updateItemDto)).thenReturn(expectedResponse);

        ResponseEntity<Object> response = itemController.updateItem(itemId, updateItemDto, userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Item updated", response.getBody());
        verify(itemClient, times(1)).updateItem(userId, itemId, updateItemDto);
    }

    @Test
    void getItem_shouldReturnResponse_whenItemExists() {
        Long userId = 1L;
        Long itemId = 1L;

        ResponseEntity<Object> expectedResponse = ResponseEntity.ok("Item details");
        when(itemClient.getItem(userId, itemId)).thenReturn(expectedResponse);

        ResponseEntity<Object> response = itemController.getItem(itemId, userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Item details", response.getBody());
        verify(itemClient, times(1)).getItem(userId, itemId);
    }

    @Test
    void getItemsByOwner_shouldReturnResponse_whenItemsExist() {
        Long userId = 1L;

        ResponseEntity<Object> expectedResponse = ResponseEntity.ok(List.of("Item1", "Item2"));
        when(itemClient.getItemsByOwner(userId)).thenReturn(expectedResponse);

        ResponseEntity<Object> response = itemController.getItemsByOwner(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(List.of("Item1", "Item2"), response.getBody());
        verify(itemClient, times(1)).getItemsByOwner(userId);
    }

    @Test
    void searchItems_shouldReturnResponse_whenItemsMatchSearch() {
        String text = "search text";
        int from = 0;
        int size = 10;

        ResponseEntity<Object> expectedResponse = ResponseEntity.ok(List.of("SearchItem1", "SearchItem2"));
        when(itemClient.searchItems(text, from, size)).thenReturn(expectedResponse);

        ResponseEntity<Object> response = itemController.searchItems(text, from, size);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(List.of("SearchItem1", "SearchItem2"), response.getBody());
        verify(itemClient, times(1)).searchItems(text, from, size);
    }
}
