package ru.practicum.shareit.features.booking.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.features.booking.common.BookingStatus;
import ru.practicum.shareit.features.booking.dto.BookingDto;
import ru.practicum.shareit.features.booking.dto.CreateBookingDto;
import ru.practicum.shareit.features.booking.service.BookingService;

import java.util.List;

import static ru.practicum.shareit.config.HTTPHeadersConfig.X_SHARER_USER_ID;

@RestController
@RequestMapping(path = "/bookings")
@Slf4j
public class BookingController {

    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<BookingDto> createBooking(@RequestBody CreateBookingDto bookingDto,
                                                    @RequestHeader(X_SHARER_USER_ID) Long bookerId) {
        log.info("Received request to create booking: bookerId={}, bookingDto={}", bookerId, bookingDto);
        BookingDto createdBooking = bookingService.createBooking(bookerId, bookingDto);
        log.info("Created booking: {}", createdBooking);
        return ResponseEntity.ok(createdBooking);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<BookingDto> updateBookingStatus(@RequestHeader(X_SHARER_USER_ID) Long bookerId,
                                                          @PathVariable Long bookingId,
                                                          @RequestParam boolean approved) {
        log.info("Received request to update booking status: bookingId={}, bookerId={}, approved={}", bookingId, bookerId, approved);
        BookingDto updatedBooking = bookingService.updateBookingStatus(bookerId, bookingId, approved);
        log.info("Updated booking: {}", updatedBooking);
        return ResponseEntity.ok(updatedBooking);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingDto> getBookingById(@PathVariable Long bookingId) {
        log.info("Received request to get booking by ID: {}", bookingId);
        BookingDto booking = bookingService.getBookingById(bookingId);
        log.info("Retrieved booking: {}", booking);
        return ResponseEntity.ok(booking);
    }

    @GetMapping
    public ResponseEntity<List<BookingDto>> getUserBookings(@RequestHeader(X_SHARER_USER_ID) Long bookerId,
                                                            @RequestParam(defaultValue = "WAITING") BookingStatus state) {
        log.info("Received request to get user bookings: bookerId={}, state={}", bookerId, state);
        List<BookingDto> bookings = bookingService.getUserBookings(bookerId, state);
        log.info("Retrieved {} bookings for user {}", bookings.size(), bookerId);
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/owner")
    public ResponseEntity<List<BookingDto>> getOwnerBookings(@RequestHeader(X_SHARER_USER_ID) Long ownerId,
                                                             @RequestParam(defaultValue = "WAITING") BookingStatus state) {
        log.info("Received request to get owner bookings: ownerId={}, state={}", ownerId, state);
        List<BookingDto> bookings = bookingService.getOwnerBookings(ownerId, state);
        log.info("Retrieved {} bookings for owner {}", bookings.size(), ownerId);
        return ResponseEntity.ok(bookings);
    }
}
