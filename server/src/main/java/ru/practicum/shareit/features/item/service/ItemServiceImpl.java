package ru.practicum.shareit.features.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.common.exceptions.ValidationException;
import ru.practicum.shareit.features.booking.common.BookingStatus;
import ru.practicum.shareit.features.booking.model.Booking;
import ru.practicum.shareit.features.booking.repository.BookingRepository;
import ru.practicum.shareit.features.item.dto.*;
import ru.practicum.shareit.features.item.exception.ItemNotFound;
import ru.practicum.shareit.features.item.mapper.CommentMapper;
import ru.practicum.shareit.features.item.mapper.ItemMapper;
import ru.practicum.shareit.features.item.model.Comment;
import ru.practicum.shareit.features.item.model.Item;
import ru.practicum.shareit.features.item.repository.CommentRepository;
import ru.practicum.shareit.features.item.repository.ItemRepository;
import ru.practicum.shareit.features.user.exception.UserNotFound;
import ru.practicum.shareit.features.user.model.User;
import ru.practicum.shareit.features.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    public ItemDto createItem(CreateItemDto createItemDto, Long userId) {
        User owner = getUserById(userId);
        Item item = ItemMapper.toItem(createItemDto, owner);
        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    public ItemDto updateItem(Long itemId, UpdateItemDto updateItemDto, Long userId) {
        Item item = getItemById(itemId);
        if (!item.getOwner().getId().equals(userId)) {
            throw new ValidationException("Редактировать вещь может только её владелец", HttpStatus.FORBIDDEN);
        }
        Item updatedItem = ItemMapper.updateItem(item, updateItemDto);
        return ItemMapper.toItemDto(itemRepository.save(updatedItem));
    }

    public BookingItemDto getItemById(Long itemId, Long userId) {
        Item item = getItemById(itemId);
        LocalDateTime lastBooking = null;
        LocalDateTime nextBooking = null;

        if (item.getOwner().getId().equals(userId)) {
            lastBooking = getLastBookingForItem(itemId);
            nextBooking = getNextBookingForItem(itemId);
        }

        List<CommentDto> comments = getCommentsForItem(itemId);

        return ItemMapper.toItemDto(item, lastBooking, nextBooking, comments);
    }

    public List<ItemDto> getItemsByOwner(Long ownerId) {
        getUserById(ownerId);
        return itemRepository.findByOwnerId(ownerId)
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    public List<ItemDto> searchItems(String text) {
        if (text == null || text.isBlank()) {
            return List.of();
        }
        return itemRepository.searchByText(text.toLowerCase())
                .stream()
                .filter(Item::isAvailable)
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFound(userId));
    }

    private Item getItemById(Long itemId) {
        return itemRepository.findById(itemId).orElseThrow(() -> new ItemNotFound(itemId));
    }

    @Transactional
    public CommentDto addComment(Long itemId, Long userId, CommentDto commentDto) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new ItemNotFound(itemId));
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFound(userId));

        boolean hasBooking = bookingRepository.existsByItemIdAndBookerIdAndStatusAndEndBefore(
                itemId, userId, BookingStatus.APPROVED, LocalDateTime.now());

        if (!hasBooking) {
            throw new ValidationException("Комментарий может оставить только пользователь, " +
                    "арендовавший вещь, после завершения срока аренды", HttpStatus.BAD_REQUEST);
        }

        Comment comment = new Comment();
        comment.setText(commentDto.getText());
        comment.setItem(item);
        comment.setAuthor(user);
        comment.setCreated(LocalDateTime.now());

        Comment savedComment = commentRepository.save(comment);

        return CommentMapper.toDto(savedComment);
    }

    private LocalDateTime getLastBookingForItem(Long itemId) {
        return bookingRepository.findLastCompletedBooking(itemId, LocalDateTime.now())
                .map(Booking::getStart)
                .orElse(null);
    }

    private LocalDateTime getNextBookingForItem(Long itemId) {
        return bookingRepository.findNextBooking(itemId, LocalDateTime.now())
                .map(Booking::getStart)
                .orElse(null);
    }

    private List<CommentDto> getCommentsForItem(Long itemId) {
        List<Comment> comments = commentRepository.findByItemId(itemId);
        return comments.stream()
                .map(comment -> new CommentDto(comment.getId(), comment.getText(), comment.getAuthor().getName(), comment.getCreated()))
                .collect(Collectors.toList());
    }
}
