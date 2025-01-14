package ru.practicum.shareit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.practicum.shareit.features.user.controller.UserController;
import ru.practicum.shareit.features.user.dto.CreateUserRequest;
import ru.practicum.shareit.features.user.dto.UpdateUserRequest;
import ru.practicum.shareit.features.user.dto.UserDto;
import ru.practicum.shareit.features.user.service.UserService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserControllerTest {

    private UserService userService;
    private UserController userController;

    @BeforeEach
    void setUp() {
        userService = mock(UserService.class);
        userController = new UserController(userService);
    }

    @Test
    void createUser_shouldReturnCreatedUser_whenValidInput() {
        CreateUserRequest request = new CreateUserRequest("John Doe", "john.doe@example.com");
        UserDto expectedUser = new UserDto(1L, "John Doe", "john.doe@example.com");

        when(userService.createUser(request.getName(), request.getEmail())).thenReturn(expectedUser);

        ResponseEntity<UserDto> response = userController.createUser(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(expectedUser, response.getBody());
        verify(userService, times(1)).createUser(request.getName(), request.getEmail());
    }

    @Test
    void getAllUsers_shouldReturnListOfUsers_whenUsersExist() {
        List<UserDto> expectedUsers = List.of(
                new UserDto(1L, "John Doe", "john.doe@example.com"),
                new UserDto(2L, "Jane Smith", "jane.smith@example.com")
        );

        when(userService.getAllUsers()).thenReturn(expectedUsers);

        List<UserDto> actualUsers = userController.getAllUsers();

        assertEquals(expectedUsers, actualUsers);
        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void getUser_shouldReturnUser_whenUserExists() {
        Long userId = 1L;
        UserDto expectedUser = new UserDto(userId, "John Doe", "john.doe@example.com");

        when(userService.getUserById(userId)).thenReturn(expectedUser);

        ResponseEntity<UserDto> response = userController.getUser(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedUser, response.getBody());
        verify(userService, times(1)).getUserById(userId);
    }

    @Test
    void getUser_shouldReturnNotFound_whenUserDoesNotExist() {
        Long userId = 1L;

        when(userService.getUserById(userId)).thenReturn(null);

        ResponseEntity<UserDto> response = userController.getUser(userId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(userService, times(1)).getUserById(userId);
    }

    @Test
    void updateUser_shouldReturnUpdatedUser_whenValidInput() {
        Long userId = 1L;
        UpdateUserRequest request = new UpdateUserRequest("John Updated", "updated.email@example.com");
        UserDto expectedUser = new UserDto(userId, "John Updated", "updated.email@example.com");

        when(userService.updateUser(userId, request.getName(), request.getEmail())).thenReturn(expectedUser);

        ResponseEntity<UserDto> response = userController.updateUser(userId, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedUser, response.getBody());
        verify(userService, times(1)).updateUser(userId, request.getName(), request.getEmail());
    }

    @Test
    void deleteUser_shouldReturnNoContent_whenUserDeleted() {
        Long userId = 1L;

        doNothing().when(userService).deleteUser(userId);

        ResponseEntity<Void> response = userController.deleteUser(userId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(userService, times(1)).deleteUser(userId);
    }
}
