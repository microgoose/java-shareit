package ru.practicum.shareit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.practicum.shareit.user.UserClient;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.dto.UserDto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserControllerTest {

    private UserClient userClient;
    private UserController userController;

    @BeforeEach
    void setUp() {
        userClient = mock(UserClient.class);
        userController = new UserController(userClient);
    }

    @Test
    void createUser_shouldReturnResponse_whenUserIsCreated() {
        UserDto userDto = new UserDto("John Doe", "john.doe@example.com");
        ResponseEntity<Object> expectedResponse = ResponseEntity.status(HttpStatus.CREATED).body("User created");

        when(userClient.createUser(userDto)).thenReturn(expectedResponse);

        ResponseEntity<Object> response = userController.createUser(userDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("User created", response.getBody());
        verify(userClient, times(1)).createUser(userDto);
    }

    @Test
    void getAllUsers_shouldReturnResponse_whenUsersExist() {
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok("List of users");
        when(userClient.getAllUsers()).thenReturn(expectedResponse);

        ResponseEntity<Object> response = userController.getAllUsers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("List of users", response.getBody());
        verify(userClient, times(1)).getAllUsers();
    }

    @Test
    void getUser_shouldReturnResponse_whenUserExists() {
        Long userId = 1L;
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok("User details");

        when(userClient.getUser(userId)).thenReturn(expectedResponse);

        ResponseEntity<Object> response = userController.getUser(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User details", response.getBody());
        verify(userClient, times(1)).getUser(userId);
    }

    @Test
    void updateUser_shouldReturnResponse_whenUserIsUpdated() {
        Long userId = 1L;
        UserDto userDto = new UserDto("Jane Doe", "jane.doe@example.com");
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok("User updated");

        when(userClient.updateUser(userId, userDto)).thenReturn(expectedResponse);

        ResponseEntity<Object> response = userController.updateUser(userId, userDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User updated", response.getBody());
        verify(userClient, times(1)).updateUser(userId, userDto);
    }

    @Test
    void deleteUser_shouldReturnResponse_whenUserIsDeleted() {
        Long userId = 1L;
        ResponseEntity<Object> expectedResponse = ResponseEntity.noContent().build();

        when(userClient.deleteUser(userId)).thenReturn(expectedResponse);

        ResponseEntity<Object> response = userController.deleteUser(userId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(userClient, times(1)).deleteUser(userId);
    }
}
