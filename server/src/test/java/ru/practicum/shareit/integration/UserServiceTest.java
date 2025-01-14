package ru.practicum.shareit.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.practicum.shareit.features.user.dto.UserDto;
import ru.practicum.shareit.features.user.exception.EmailExistException;
import ru.practicum.shareit.features.user.exception.UserNotFound;
import ru.practicum.shareit.features.user.model.User;
import ru.practicum.shareit.features.user.repository.UserRepository;
import ru.practicum.shareit.features.user.service.UserService;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setName("Test User");
        testUser.setEmail("testuser" + System.currentTimeMillis() + "@example.com");
        testUser = userRepository.save(testUser);
    }

    @Test
    void updateUser_shouldUpdateUser_whenValidData() {
        String newName = "Updated User";
        String newEmail = "updateduser@example.com";

        UserDto updatedUser = userService.updateUser(testUser.getId(), newName, newEmail);

        assertNotNull(updatedUser);
        assertEquals(newName, updatedUser.getName());
        assertEquals(newEmail, updatedUser.getEmail());
    }

    @Test
    void updateUser_shouldThrowUserNotFound_whenUserDoesNotExist() {
        Long nonExistentUserId = 999L;
        UserNotFound exception = assertThrows(UserNotFound.class, () -> {
            userService.updateUser(nonExistentUserId, "New Name", "newemail@example.com");
        });

        assertEquals(nonExistentUserId, exception.userId);
    }

    @Test
    void updateUser_shouldThrowEmailExistException_whenEmailAlreadyExists() {
        User anotherUser = new User();
        anotherUser.setName("Another User");
        anotherUser.setEmail("anotheruser@example.com");
        userRepository.save(anotherUser);

        EmailExistException exception = assertThrows(EmailExistException.class, () -> {
            userService.updateUser(testUser.getId(), "New Name", "anotheruser@example.com");
        });

        assertEquals("anotheruser@example.com", exception.email);
    }

    @Test
    void updateUser_shouldNotUpdateWhenNameAndEmailAreNull() {
        UserDto updatedUser = userService.updateUser(testUser.getId(), null, null);

        assertNotNull(updatedUser);
        assertEquals(testUser.getName(), updatedUser.getName());
        assertEquals(testUser.getEmail(), updatedUser.getEmail());
    }
}
