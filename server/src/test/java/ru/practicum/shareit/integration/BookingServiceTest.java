package ru.practicum.shareit.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import ru.practicum.shareit.common.exceptions.ValidationException;
import ru.practicum.shareit.features.booking.common.BookingStatus;
import ru.practicum.shareit.features.booking.dto.BookingDto;
import ru.practicum.shareit.features.booking.dto.CreateBookingDto;
import ru.practicum.shareit.features.booking.repository.BookingRepository;
import ru.practicum.shareit.features.booking.service.BookingService;
import ru.practicum.shareit.features.item.exception.ItemNotFound;
import ru.practicum.shareit.features.item.model.Item;
import ru.practicum.shareit.features.item.repository.ItemRepository;
import ru.practicum.shareit.features.user.exception.UserNotFound;
import ru.practicum.shareit.features.user.model.User;
import ru.practicum.shareit.features.user.repository.UserRepository;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class BookingServiceTest {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    private User booker;
    private User owner;
    private Item item;

    private static int counter = 0;

    @BeforeEach
    void setUp() {
        String uniqueKey = String.valueOf(counter++);

        owner = createUser(uniqueKey, "Owner");
        booker = createUser(uniqueKey, "Booker");
        item = createItem(uniqueKey, owner);
    }

    private User createUser(String key, String role) {
        String name = role + key;
        String email = role.toLowerCase() + key + "@example.com";
        return userRepository.save(new User(name, email));
    }

    private Item createItem(String key, User owner) {
        Item newItem = new Item();
        newItem.setName("Item " + key);
        newItem.setDescription("Description of item " + key);
        newItem.setAvailable(true);
        newItem.setOwner(owner);
        return itemRepository.save(newItem);
    }

    @Test
    void createBooking_shouldCreateBooking_whenValidData() {
        CreateBookingDto createBookingDto = new CreateBookingDto();
        createBookingDto.setItemId(item.getId());
        createBookingDto.setStart(LocalDateTime.now().plusDays(1));
        createBookingDto.setEnd(LocalDateTime.now().plusDays(2));

        BookingDto bookingDto = bookingService.createBooking(booker.getId(), createBookingDto);

        assertNotNull(bookingDto.getId());
        assertEquals(BookingStatus.WAITING.toString(), bookingDto.getStatus());
        assertEquals(booker.getId(), bookingDto.getBooker().getId());
        assertEquals(item.getId(), bookingDto.getItem().getId());
    }

    @Test
    void createBooking_shouldThrowValidationException_whenItemNotAvailable() {
        item.setAvailable(false);
        itemRepository.save(item);
        CreateBookingDto createBookingDto = new CreateBookingDto();
        createBookingDto.setItemId(item.getId());
        createBookingDto.setStart(LocalDateTime.now().plusDays(1));
        createBookingDto.setEnd(LocalDateTime.now().plusDays(2));

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            bookingService.createBooking(booker.getId(), createBookingDto);
        });
        assertEquals(HttpStatus.BAD_REQUEST, exception.httpStatus);
        assertEquals("Вещь недоступна", exception.getMessage());
    }

    @Test
    void createBooking_shouldThrowValidationException_whenItemIdNotProvided() {
        CreateBookingDto createBookingDto = new CreateBookingDto();
        createBookingDto.setStart(LocalDateTime.now().plusDays(1));
        createBookingDto.setEnd(LocalDateTime.now().plusDays(2));

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            bookingService.createBooking(booker.getId(), createBookingDto);
        });
        assertEquals(HttpStatus.NOT_FOUND, exception.httpStatus);
        assertEquals("Не указана вещь", exception.getMessage());
    }

    @Test
    void createBooking_shouldThrowUserNotFoundException_whenUserDoesNotExist() {
        Long nonExistentUserId = 999L;
        CreateBookingDto createBookingDto = new CreateBookingDto();
        createBookingDto.setItemId(item.getId());
        createBookingDto.setStart(LocalDateTime.now().plusDays(1));
        createBookingDto.setEnd(LocalDateTime.now().plusDays(2));

        UserNotFound exception = assertThrows(UserNotFound.class, () -> {
            bookingService.createBooking(nonExistentUserId, createBookingDto);
        });
        assertEquals(nonExistentUserId, exception.userId);
    }

    @Test
    void createBooking_shouldThrowItemNotFoundException_whenItemDoesNotExist() {
        Long nonExistentItemId = 999L;
        CreateBookingDto createBookingDto = new CreateBookingDto();
        createBookingDto.setItemId(nonExistentItemId);
        createBookingDto.setStart(LocalDateTime.now().plusDays(1));
        createBookingDto.setEnd(LocalDateTime.now().plusDays(2));

        ItemNotFound exception = assertThrows(ItemNotFound.class, () -> {
            bookingService.createBooking(booker.getId(), createBookingDto);
        });
        assertEquals(nonExistentItemId, exception.itemId);
    }
}
