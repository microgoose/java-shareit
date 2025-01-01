package ru.practicum.shareit.features.booking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.features.booking.common.BookingStatus;
import ru.practicum.shareit.features.booking.dto.BookingDto;
import ru.practicum.shareit.features.booking.dto.CreateBookingDto;
import ru.practicum.shareit.features.booking.service.BookingService;

import java.util.List;

import static ru.practicum.shareit.features.item.config.UserRequestConfig.X_SHARER_USER_ID;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<BookingDto> createBooking(@RequestBody CreateBookingDto bookingDto,
                                                    @RequestHeader(X_SHARER_USER_ID) Long bookerId) {
        BookingDto createdBooking = bookingService.createBooking(bookerId, bookingDto);
        return ResponseEntity.ok(createdBooking);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<BookingDto> updateBookingStatus(@RequestHeader(X_SHARER_USER_ID) Long bookerId,
                                                          @PathVariable Long bookingId,
                                                          @RequestParam boolean approved) {
        BookingDto updatedBooking = bookingService.updateBookingStatus(bookerId, bookingId, approved);
        return ResponseEntity.ok(updatedBooking);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingDto> getBookingById(@PathVariable Long bookingId) {
        BookingDto booking = bookingService.getBookingById(bookingId);
        return ResponseEntity.ok(booking);
    }

    @GetMapping
    public ResponseEntity<List<BookingDto>> getUserBookings(@RequestHeader(X_SHARER_USER_ID) Long bookerId,
                                                            @RequestParam(defaultValue = "WAITING") BookingStatus state) {
        List<BookingDto> bookings = bookingService.getUserBookings(bookerId, state);
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/owner")
    public ResponseEntity<List<BookingDto>> getOwnerBookings(@RequestHeader(X_SHARER_USER_ID) Long ownerId,
                                                             @RequestParam(defaultValue = "WAITING") BookingStatus state) {
        List<BookingDto> bookings = bookingService.getOwnerBookings(ownerId, state);
        return ResponseEntity.ok(bookings);
    }
}
