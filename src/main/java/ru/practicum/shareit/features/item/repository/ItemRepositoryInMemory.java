package ru.practicum.shareit.features.item.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.features.item.model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class ItemRepositoryInMemory implements ItemRepository {
    private final Map<Long, Item> items = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong();

    public Item save(Item item) {
        if (item.getId() == null) {
            item.setId(idGenerator.incrementAndGet());
        }
        items.put(item.getId(), item);
        return item;
    }

    public Item findById(Long id) {
        return items.get(id);
    }

    public void deleteById(Long id) {
        items.remove(id);
    }

    public List<Item> findByOwnerId(Long ownerId) {
        return items.values().stream()
                .filter(item -> item.getOwner().getId().equals(ownerId))
                .collect(Collectors.toList());
    }

    public List<Item> searchByText(String text) {
        String lowerText = text.toLowerCase();
        return items.values().stream()
                .filter(item -> (item.getName() != null && item.getName().toLowerCase().contains(lowerText)) ||
                        (item.getDescription() != null && item.getDescription().toLowerCase().contains(lowerText)))
                .collect(Collectors.toList());
    }

    public List<Item> findAll() {
        return new ArrayList<>(items.values());
    }
}
