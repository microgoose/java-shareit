package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingStatus;
import ru.practicum.shareit.booking.dto.CreateBookingDto;
import ru.practicum.shareit.config.HTTPHeadersConfig;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {

	private final BookingClient bookingClient;

	@PostMapping
	public ResponseEntity<Object> createBooking(@RequestBody @Valid CreateBookingDto bookingDto,
												@RequestHeader(HTTPHeadersConfig.X_SHARER_USER_ID) Long bookerId) {
		log.info("Creating booking {}, bookerId={}", bookingDto, bookerId);
		return bookingClient.createBooking(bookerId, bookingDto);
	}

	@PatchMapping("/{bookingId}")
	public ResponseEntity<Object> updateBookingStatus(@RequestHeader(HTTPHeadersConfig.X_SHARER_USER_ID) Long bookerId,
													  @PathVariable @Positive Long bookingId,
													  @RequestParam boolean approved) {
		log.info("Updating booking status for bookingId={}, bookerId={}, approved={}", bookingId, bookerId, approved);
		return bookingClient.updateBookingStatus(bookerId, bookingId, approved);
	}

	@GetMapping("/{bookingId}")
	public ResponseEntity<Object> getBookingById(@PathVariable @Positive Long bookingId) {
		log.info("Getting booking by id {}", bookingId);
		return bookingClient.getBooking(bookingId);
	}

	@GetMapping
	public ResponseEntity<Object> getUserBookings(@RequestHeader(HTTPHeadersConfig.X_SHARER_USER_ID) Long bookerId,
												  @RequestParam(defaultValue = "WAITING") BookingStatus state,
												  @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
												  @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
		log.info("Getting bookings for bookerId={}, state={}, from={}, size={}", bookerId, state, from, size);
		return bookingClient.getUserBookings(bookerId, state, from, size);
	}

	@GetMapping("/owner")
	public ResponseEntity<Object> getOwnerBookings(@RequestHeader(HTTPHeadersConfig.X_SHARER_USER_ID) Long ownerId,
												   @RequestParam(defaultValue = "WAITING") BookingStatus state,
												   @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
												   @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
		log.info("Getting owner bookings for ownerId={}, state={}, from={}, size={}", ownerId, state, from, size);
		return bookingClient.getOwnerBookings(ownerId, state, from, size);
	}
}