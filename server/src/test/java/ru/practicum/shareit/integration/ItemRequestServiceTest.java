package ru.practicum.shareit.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.practicum.shareit.features.request.dto.ItemRequestDto;
import ru.practicum.shareit.features.request.model.ItemRequest;
import ru.practicum.shareit.features.request.repository.ItemRequestRepository;
import ru.practicum.shareit.features.request.service.ItemRequestService;
import ru.practicum.shareit.features.user.model.User;
import ru.practicum.shareit.features.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class ItemRequestServiceTest {

    @Autowired
    private ItemRequestService itemRequestService;

    @Autowired
    private ItemRequestRepository itemRequestRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = userRepository.save(new User("TestUser", "testuser" + System.currentTimeMillis() + "@example.com"));
    }

    @Test
    void getUserRequests_shouldReturnUserRequests_whenUserHasRequests() {
        ItemRequest request1 = new ItemRequest();
        request1.setRequestor(user);
        request1.setDescription("Description");
        request1.setCreated(LocalDateTime.now());
        itemRequestRepository.save(request1);

        ItemRequest request2 = new ItemRequest();
        request2.setRequestor(user);
        request2.setDescription("Description");
        request2.setCreated(LocalDateTime.now().minusDays(1));
        itemRequestRepository.save(request2);

        List<ItemRequestDto> requests = itemRequestService.getUserRequests(user.getId());

        assertNotNull(requests);
        assertEquals(2, requests.size());
        assertEquals(user.getId(), requests.get(0).getRequestorId());
        assertEquals(user.getId(), requests.get(1).getRequestorId());
    }

    @Test
    void getUserRequests_shouldReturnEmptyList_whenUserHasNoRequests() {
        List<ItemRequestDto> requests = itemRequestService.getUserRequests(user.getId());

        assertNotNull(requests);
        assertTrue(requests.isEmpty());
    }
}
