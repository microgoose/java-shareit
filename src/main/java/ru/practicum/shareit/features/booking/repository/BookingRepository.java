package ru.practicum.shareit.features.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.features.booking.common.BookingStatus;
import ru.practicum.shareit.features.booking.model.Booking;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("SELECT b FROM Booking b WHERE b.booker.id = :userId AND (:state = 'ALL' OR b.status = :state)")
    List<Booking> findBookingsByUserAndState(Long userId, BookingStatus state);

    @Query("SELECT b FROM Booking b JOIN b.item i WHERE i.owner.id = :ownerId AND b.status = :state")
    List<Booking> findBookingsByOwnerAndState(Long ownerId, BookingStatus state);
}

