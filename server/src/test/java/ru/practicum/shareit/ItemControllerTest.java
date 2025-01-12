package ru.practicum.shareit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.features.item.controller.ItemController;
import ru.practicum.shareit.features.item.dto.*;
import ru.practicum.shareit.features.item.service.ItemService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ItemControllerTest {

    private ItemService itemService;
    private ItemController itemController;

    @BeforeEach
    void setUp() {
        itemService = mock(ItemService.class);
        itemController = new ItemController(itemService);
    }

    @Test
    void addItem_shouldReturnItemDto_whenValidRequest() {
        Long userId = 1L;
        CreateItemDto createItemDto = new CreateItemDto("Item", "Description", true, null);
        ItemDto expectedItemDto = new ItemDto(1L, "Item", "Description", true, null);

        when(itemService.createItem(createItemDto, userId)).thenReturn(expectedItemDto);

        ItemDto result = itemController.addItem(createItemDto, userId);

        assertEquals(expectedItemDto, result);
        verify(itemService, times(1)).createItem(createItemDto, userId);
    }

    @Test
    void updateItem_shouldReturnUpdatedItemDto_whenValidRequest() {
        Long userId = 1L;
        Long itemId = 1L;
        UpdateItemDto updateItemDto = new UpdateItemDto("Updated Item", "Updated Description", false);
        ItemDto expectedItemDto = new ItemDto(itemId, "Updated Item", "Updated Description", false, null);

        when(itemService.updateItem(itemId, updateItemDto, userId)).thenReturn(expectedItemDto);

        ItemDto result = itemController.updateItem(itemId, updateItemDto, userId);

        assertEquals(expectedItemDto, result);
        verify(itemService, times(1)).updateItem(itemId, updateItemDto, userId);
    }

    @Test
    void getItem_shouldReturnItemDto_whenItemExists() {
        Long userId = 1L;
        Long itemId = 1L;
        BookingItemDto expectedItemDto = new BookingItemDto(itemId, "Item", "Description", true, null, null, null, List.of());

        when(itemService.getItemById(itemId, userId)).thenReturn(expectedItemDto);

        BookingItemDto result = itemController.getItem(itemId, userId);

        assertEquals(expectedItemDto, result);
        verify(itemService, times(1)).getItemById(itemId, userId);
    }

    @Test
    void getItemsByOwner_shouldReturnListOfItems_whenOwnerHasItems() {
        Long userId = 1L;
        List<ItemDto> expectedItems = List.of(
                new ItemDto(1L, "Item1", "Description1", true, null),
                new ItemDto(2L, "Item2", "Description2", true, null)
        );

        when(itemService.getItemsByOwner(userId)).thenReturn(expectedItems);

        List<ItemDto> result = itemController.getItemsByOwner(userId);

        assertEquals(expectedItems, result);
        verify(itemService, times(1)).getItemsByOwner(userId);
    }

    @Test
    void searchItems_shouldReturnListOfItems_whenSearchTextIsProvided() {
        String searchText = "item";
        List<ItemDto> expectedItems = List.of(
                new ItemDto(1L, "Item1", "Description1", true, null),
                new ItemDto(2L, "Item2", "Description2", true, null)
        );

        when(itemService.searchItems(searchText)).thenReturn(expectedItems);

        List<ItemDto> result = itemController.searchItems(searchText);

        assertEquals(expectedItems, result);
        verify(itemService, times(1)).searchItems(searchText);
    }

    @Test
    void addComment_shouldReturnCommentDto_whenValidRequest() {
        Long itemId = 1L;
        Long userId = 1L;
        CommentDto commentDto = new CommentDto(null, "Great item!", "User", null);
        CommentDto expectedCommentDto = new CommentDto(1L, "Great item!", "User", null);

        when(itemService.addComment(itemId, userId, commentDto)).thenReturn(expectedCommentDto);

        CommentDto result = itemController.addComment(itemId, commentDto, userId);

        assertEquals(expectedCommentDto, result);
        verify(itemService, times(1)).addComment(itemId, userId, commentDto);
    }
}
