package ru.practicum.shareit.features.item.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.features.request.model.ItemRequest;
import ru.practicum.shareit.features.user.model.User;

@Data
@NoArgsConstructor
public class Item {
    private Long id;
    private String name;
    private String description;
    private boolean available;
    private User owner;
    private ItemRequest request;
}
