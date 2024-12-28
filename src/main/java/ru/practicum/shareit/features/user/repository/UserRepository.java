package ru.practicum.shareit.features.user.repository;

import ru.practicum.shareit.features.user.model.User;

import java.util.List;

public interface UserRepository {
    List<User> findAll();

    User findById(Long id);

    User save(User user);

    boolean deleteById(Long id);

    boolean containsEmail(String email);

    boolean containsEmail(Long id, String email);
}
