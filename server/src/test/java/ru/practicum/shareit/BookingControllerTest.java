package ru.practicum.shareit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.features.booking.common.BookingStatus;
import ru.practicum.shareit.features.booking.controller.BookingController;
import ru.practicum.shareit.features.booking.dto.BookingDto;
import ru.practicum.shareit.features.booking.dto.CreateBookingDto;
import ru.practicum.shareit.features.booking.service.BookingService;
import ru.practicum.shareit.features.item.dto.ItemDto;
import ru.practicum.shareit.features.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingService bookingService;

    private BookingDto bookingDto;

    @BeforeEach
    void setUp() {
        bookingDto = new BookingDto(
            1L,
            LocalDateTime.now(),
            LocalDateTime.now().plusDays(1),
            new ItemDto(1L),
            new UserDto(1L),
            BookingStatus.WAITING.toString()
        );
    }

    @Test
    void createBooking_shouldReturnCreatedBooking() throws Exception {
        Mockito.when(bookingService.createBooking(anyLong(), any(CreateBookingDto.class)))
                .thenReturn(bookingDto);

        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .content("{\"itemId\": 1, \"start\": \"2025-01-15T10:00:00\", \"end\": \"2025-01-16T10:00:00\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.status").value("WAITING"))
                .andDo(print());
    }

    @Test
    void updateBookingStatus_shouldReturnUpdatedBooking() throws Exception {
        bookingDto.setStatus(BookingStatus.APPROVED.toString());
        Mockito.when(bookingService.updateBookingStatus(anyLong(), anyLong(), eq(true)))
                .thenReturn(bookingDto);

        mockMvc.perform(patch("/bookings/1")
                        .header("X-Sharer-User-Id", 1L)
                        .param("approved", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.status").value("APPROVED"))
                .andDo(print());
    }

    @Test
    void getBookingById_shouldReturnBooking() throws Exception {
        Mockito.when(bookingService.getBookingById(anyLong()))
                .thenReturn(bookingDto);

        mockMvc.perform(get("/bookings/1")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andDo(print());
    }

    @Test
    void getUserBookings_shouldReturnListOfBookings() throws Exception {
        Mockito.when(bookingService.getUserBookings(anyLong(), any(BookingStatus.class)))
                .thenReturn(List.of(bookingDto));

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", "WAITING"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andDo(print());
    }

    @Test
    void getOwnerBookings_shouldReturnListOfBookings() throws Exception {
        Mockito.when(bookingService.getOwnerBookings(anyLong(), any(BookingStatus.class)))
                .thenReturn(Collections.singletonList(bookingDto));

        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", "WAITING"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andDo(print());
    }
}
