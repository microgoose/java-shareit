package ru.practicum.shareit.features.user.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.features.user.dto.UserDto;
import ru.practicum.shareit.features.user.exception.EmailExistException;
import ru.practicum.shareit.features.user.exception.UserNotFound;
import ru.practicum.shareit.features.user.mapper.UserMapper;
import ru.practicum.shareit.features.user.model.User;
import ru.practicum.shareit.features.user.repository.UserRepository;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDto createUser(String name, String email) {
        if (userRepository.containsEmail(email)) {
            throw new EmailExistException(email);
        }

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user = userRepository.save(user);
        return UserMapper.toUserDto(user);
    }

    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserMapper::toUserDto)
                .toList();
    }

    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id);
        return user != null ? UserMapper.toUserDto(user) : null;
    }

    public UserDto updateUser(Long id, String name, String email) {
        User user = userRepository.findById(id);
        if (user == null) {
            throw new UserNotFound(id);
        }
        if (userRepository.containsEmail(id, email)) {
            throw new EmailExistException(email);
        }
        if (name != null && !name.isBlank()) {
            user.setName(name);
        }
        if (email != null && !email.isBlank()) {
            user.setEmail(email);
        }
        user = userRepository.save(user);
        return UserMapper.toUserDto(user);
    }

    public boolean deleteUser(Long id) {
        return userRepository.deleteById(id);
    }
}