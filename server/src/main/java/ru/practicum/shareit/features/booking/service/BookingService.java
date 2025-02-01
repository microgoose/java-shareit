package ru.practicum.shareit.features.booking.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.common.exceptions.ValidationException;
import ru.practicum.shareit.features.booking.common.BookingStatus;
import ru.practicum.shareit.features.booking.dto.BookingDto;
import ru.practicum.shareit.features.booking.dto.CreateBookingDto;
import ru.practicum.shareit.features.booking.exception.BookingNotFound;
import ru.practicum.shareit.features.booking.mapper.BookingMapper;
import ru.practicum.shareit.features.booking.model.Booking;
import ru.practicum.shareit.features.booking.repository.BookingRepository;
import ru.practicum.shareit.features.item.exception.ItemNotFound;
import ru.practicum.shareit.features.item.exception.OwnerNotFound;
import ru.practicum.shareit.features.item.model.Item;
import ru.practicum.shareit.features.item.repository.ItemRepository;
import ru.practicum.shareit.features.user.exception.UserNotFound;
import ru.practicum.shareit.features.user.model.User;
import ru.practicum.shareit.features.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    public BookingService(BookingRepository bookingRepository, UserRepository userRepository, ItemRepository itemRepository) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
    }


    @Transactional
    public BookingDto createBooking(Long bookerId, CreateBookingDto createBookingDto) {
        if (createBookingDto.getItemId() == null)
            throw new ValidationException("Не указана вещь", HttpStatus.NOT_FOUND);

        User user = userRepository.findById(bookerId)
                .orElseThrow(() -> new UserNotFound(bookerId));
        Item item = itemRepository.findById(createBookingDto.getItemId())
                .orElseThrow(() -> new ItemNotFound(createBookingDto.getItemId()));

        if (!item.isAvailable())
            throw new ValidationException("Вещь недоступна", HttpStatus.BAD_REQUEST);

        Booking booking = new Booking();
        booking.setStart(createBookingDto.getStart());
        booking.setEnd(createBookingDto.getEnd());
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(BookingStatus.WAITING);

        Booking savedBooking = bookingRepository.save(booking);
        return BookingMapper.mapToDto(savedBooking);
    }

    @Transactional
    public BookingDto updateBookingStatus(Long bookerId, Long bookingId, boolean approved) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFound(bookingId));

        if (!booking.getItem().getOwner().getId().equals(bookerId)) {
            throw new IllegalArgumentException("Только владелец может одобрить или отклонить бронирование");
        }

        if (booking.getStatus() == BookingStatus.APPROVED) {
            throw new IllegalStateException("Бронирование уже одобрено и не может быть изменено");
        }

        booking.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        Booking updatedBooking = bookingRepository.save(booking);

        return BookingMapper.mapToDto(updatedBooking);
    }

    public BookingDto getBookingById(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFound(bookingId));

        return BookingMapper.mapToDto(booking);
    }

    public List<BookingDto> getUserBookings(Long bookerId, BookingStatus state) {
        List<Booking> bookings = bookingRepository.findBookingsByUserAndState(bookerId, state);
        return bookings.stream()
                .map(BookingMapper::mapToDto)
                .collect(Collectors.toList());
    }


    public List<BookingDto> getOwnerBookings(Long ownerId, BookingStatus state) {
        List<Booking> bookings = bookingRepository.findBookingsByOwnerAndState(ownerId, state);

        if (bookings.isEmpty()) {
            throw new OwnerNotFound(ownerId);
        }

        return bookings.stream()
                .map(BookingMapper::mapToDto)
                .collect(Collectors.toList());
    }
}