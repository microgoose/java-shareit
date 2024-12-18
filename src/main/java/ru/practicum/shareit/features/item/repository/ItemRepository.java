package ru.practicum.shareit.features.item.repository;

import ru.practicum.shareit.features.item.model.Item;

import java.util.List;

public interface ItemRepository {
    Item save(Item item);

    Item findById(Long id);

    void deleteById(Long id);

    List<Item> findByOwnerId(Long ownerId);

    List<Item> searchByText(String text);

    List<Item> findAll();
}
