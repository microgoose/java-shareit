package ru.practicum.shareit.features.user.repository;


import org.springframework.stereotype.Repository;
import ru.practicum.shareit.features.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class UserRepositoryInMemory implements UserRepository {
    private final ConcurrentHashMap<Long, User> userStore = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public User save(User user) {
        if (user.getId() == null) {
            user.setId(idGenerator.getAndIncrement());
        }
        userStore.put(user.getId(), user);
        return user;
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(userStore.values());
    }

    @Override
    public User findById(Long id) {
        return userStore.get(id);
    }

    @Override
    public boolean deleteById(Long id) {
        return userStore.remove(id) != null;
    }

    @Override
    public boolean containsEmail(String email) {
        return userStore.values().stream().anyMatch(user -> user.getEmail().equals(email));
    }

    @Override
    public boolean containsEmail(Long id, String email) {
        return userStore.values().stream()
                .anyMatch(user -> user.getEmail().equals(email) && !user.getId().equals(id));
    }
}
