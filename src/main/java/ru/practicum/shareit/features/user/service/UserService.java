package ru.practicum.shareit.features.user.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.features.user.dto.UserDto;
import ru.practicum.shareit.features.user.exception.EmailExistException;
import ru.practicum.shareit.features.user.exception.UserNotFound;
import ru.practicum.shareit.features.user.mapper.UserMapper;
import ru.practicum.shareit.features.user.model.User;
import ru.practicum.shareit.features.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDto createUser(String name, String email) {
        if (userRepository.existsByEmail(email)) {
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
        Optional<User> userOptional = userRepository.findById(id);
        return userOptional.map(UserMapper::toUserDto).orElse(null);
    }

    public UserDto updateUser(Long id, String name, String email) {
        Optional<User> userOptional = userRepository.findById(id);
        User user = userOptional.orElse(null);

        if (user == null) {
            throw new UserNotFound(id);
        }
        if (userRepository.existsByEmailAndNotId(id, email)) {
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

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}