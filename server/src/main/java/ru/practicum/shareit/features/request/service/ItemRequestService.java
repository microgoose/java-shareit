package ru.practicum.shareit.features.request.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.features.request.dto.ItemRequestDto;
import ru.practicum.shareit.features.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.features.request.model.ItemRequest;
import ru.practicum.shareit.features.request.repository.ItemRequestRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;

    public ItemRequestDto createRequest(ItemRequestDto itemRequestDto, Long userId) {
        ItemRequest request = ItemRequestMapper.mapToEntity(itemRequestDto, userId);
        request.setCreated(LocalDateTime.now());
        ItemRequest savedRequest = itemRequestRepository.save(request);
        return ItemRequestMapper.mapToDto(savedRequest);
    }

    @Transactional
    public List<ItemRequestDto> getUserRequests(Long userId) {
        List<ItemRequest> requests = itemRequestRepository.findByRequestorIdOrderByCreatedDesc(userId);
        return requests.stream().map(ItemRequestMapper::mapToDto).collect(Collectors.toList());
    }

    public List<ItemRequestDto> getAllRequests(Long userId) {
        List<ItemRequest> requests = itemRequestRepository.findAllExcludingUserId(userId);
        return requests.stream().map(ItemRequestMapper::mapToDto).collect(Collectors.toList());
    }

    public ItemRequestDto getRequestById(Long requestId) {
        ItemRequest request = itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Request not found"));
        return ItemRequestMapper.mapToDto(request);
    }
}