package ru.practicum.shareit.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.practicum.shareit.features.item.dto.CreateItemDto;
import ru.practicum.shareit.features.item.dto.ItemDto;
import ru.practicum.shareit.features.item.service.ItemServiceImpl;
import ru.practicum.shareit.features.user.model.User;
import ru.practicum.shareit.features.user.repository.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
public class ItemServiceImplTest {

    @Autowired
    private ItemServiceImpl itemService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void getItemsByOwner_shouldReturnItems_whenOwnerHasItems() {
        Long userId = userRepository.save(new User("Test User", "test@example.com")).getId();
        CreateItemDto createItemDto1 = new CreateItemDto("Item 1", "Description 1", true, null);
        CreateItemDto createItemDto2 = new CreateItemDto("Item 2", "Description 2", true, null);
        itemService.createItem(createItemDto1, userId);
        itemService.createItem(createItemDto2, userId);

        List<ItemDto> items = itemService.getItemsByOwner(userId);

        assertEquals(2, items.size());
        assertEquals("Item 1", items.get(0).getName());
        assertEquals("Item 2", items.get(1).getName());
    }

    @Test
    void getItemsByOwner_shouldReturnEmptyList_whenOwnerHasNoItems() {
        Long userId = userRepository.save(new User("Test User", "test2@example.com")).getId();
        List<ItemDto> items = itemService.getItemsByOwner(userId);
        assertEquals(0, items.size());
    }
}

