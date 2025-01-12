package ru.practicum.shareit.features.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.features.request.model.ItemRequest;

import java.util.List;


@Repository
public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {

    @Query("SELECT r FROM ItemRequest r JOIN FETCH r.requestor LEFT JOIN FETCH r.responses WHERE r.requestor.id = :userId ORDER BY r.created DESC")
    List<ItemRequest> findByRequestorIdOrderByCreatedDesc(Long userId);

    @Query("SELECT r FROM ItemRequest r WHERE r.requestor.id <> :userId ORDER BY r.created DESC")
    List<ItemRequest> findAllExcludingUserId(Long userId);
}
