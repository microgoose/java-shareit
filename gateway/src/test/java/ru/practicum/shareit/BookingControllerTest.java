package ru.practicum.shareit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.practicum.shareit.booking.BookingClient;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.booking.dto.BookingStatus;
import ru.practicum.shareit.booking.dto.CreateBookingDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class BookingControllerTest {

    private BookingClient bookingClient;
    private BookingController bookingController;

    @BeforeEach
    void setUp() {
        bookingClient = mock(BookingClient.class);
        bookingController = new BookingController(bookingClient);
    }

    @Test
    void createBooking_shouldReturnResponse_whenBookingIsCreated() {
        Long bookerId = 1L;
        CreateBookingDto bookingDto = new CreateBookingDto();
        bookingDto.setStart(LocalDateTime.now().plusDays(1));
        bookingDto.setEnd(LocalDateTime.now().plusDays(2));
        bookingDto.setItemId(1L);

        ResponseEntity<Object> expectedResponse = ResponseEntity.status(HttpStatus.CREATED).body("Booking created");
        when(bookingClient.createBooking(bookerId, bookingDto)).thenReturn(expectedResponse);

        ResponseEntity<Object> response = bookingController.createBooking(bookingDto, bookerId);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Booking created", response.getBody());
        verify(bookingClient, times(1)).createBooking(bookerId, bookingDto);
    }

    @Test
    void updateBookingStatus_shouldReturnResponse_whenStatusUpdated() {
        long bookerId = 1L;
        long bookingId = 1L;
        boolean approved = true;

        ResponseEntity<Object> expectedResponse = ResponseEntity.ok("Booking status updated");
        when(bookingClient.updateBookingStatus(bookerId, bookingId, approved)).thenReturn(expectedResponse);

        ResponseEntity<Object> response = bookingController.updateBookingStatus(bookerId, bookingId, approved);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Booking status updated", response.getBody());
        verify(bookingClient, times(1)).updateBookingStatus(bookerId, bookingId, approved);
    }

    @Test
    void getBookingById_shouldReturnResponse_whenBookingExists() {
        Long bookingId = 1L;

        ResponseEntity<Object> expectedResponse = ResponseEntity.ok("Booking details");
        when(bookingClient.getBooking(bookingId)).thenReturn(expectedResponse);

        ResponseEntity<Object> response = bookingController.getBookingById(bookingId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Booking details", response.getBody());
        verify(bookingClient, times(1)).getBooking(bookingId);
    }

    @Test
    void getUserBookings_shouldReturnResponse_whenBookingsExist() {
        Long bookerId = 1L;
        BookingStatus state = BookingStatus.WAITING;
        int from = 0;
        int size = 10;

        ResponseEntity<Object> expectedResponse = ResponseEntity.ok(List.of("Booking1", "Booking2"));
        when(bookingClient.getUserBookings(bookerId, state, from, size)).thenReturn(expectedResponse);

        ResponseEntity<Object> response = bookingController.getUserBookings(bookerId, state, from, size);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(List.of("Booking1", "Booking2"), response.getBody());
        verify(bookingClient, times(1)).getUserBookings(bookerId, state, from, size);
    }

    @Test
    void getOwnerBookings_shouldReturnResponse_whenBookingsExist() {
        Long ownerId = 1L;
        BookingStatus state = BookingStatus.WAITING;
        int from = 0;
        int size = 10;

        ResponseEntity<Object> expectedResponse = ResponseEntity.ok(List.of("OwnerBooking1", "OwnerBooking2"));
        when(bookingClient.getOwnerBookings(ownerId, state, from, size)).thenReturn(expectedResponse);

        ResponseEntity<Object> response = bookingController.getOwnerBookings(ownerId, state, from, size);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(List.of("OwnerBooking1", "OwnerBooking2"), response.getBody());
        verify(bookingClient, times(1)).getOwnerBookings(ownerId, state, from, size);
    }
}
