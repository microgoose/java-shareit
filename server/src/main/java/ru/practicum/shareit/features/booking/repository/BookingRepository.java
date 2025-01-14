package ru.practicum.shareit.features.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.features.booking.common.BookingStatus;
import ru.practicum.shareit.features.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("SELECT b FROM Booking b WHERE b.booker.id = :userId AND (:state = 'ALL' OR b.status = :state)")
    List<Booking> findBookingsByUserAndState(Long userId, BookingStatus state);

    @Query("SELECT b FROM Booking b JOIN b.item i WHERE i.owner.id = :ownerId AND b.status = :state")
    List<Booking> findBookingsByOwnerAndState(Long ownerId, BookingStatus state);

    boolean existsByItemIdAndBookerIdAndStatusAndEndBefore(Long itemId, Long bookerId,
                                                           BookingStatus status,
                                                           LocalDateTime endBefore);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.item.id = :itemId " +
            "AND b.end < :currentTime " +
            "ORDER BY b.start DESC")
    Optional<Booking> findLastCompletedBooking(Long itemId, LocalDateTime currentTime);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.item.id = :itemId " +
            "AND b.start > :currentTime " + // Только будущие бронирования
            "ORDER BY b.start ASC")
    Optional<Booking> findNextBooking(Long itemId, LocalDateTime currentTime);
}

