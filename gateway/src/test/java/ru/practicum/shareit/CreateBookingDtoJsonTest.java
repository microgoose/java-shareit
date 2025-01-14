package ru.practicum.shareit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.CreateBookingDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class CreateBookingDtoJsonTest {

    @Autowired
    private JacksonTester<CreateBookingDto> json;

    @Test
    void testCreateBookingDtoSerialization() throws Exception {
        CreateBookingDto createBookingDto = new CreateBookingDto();
        createBookingDto.setStart(LocalDateTime.of(2025, 1, 12, 10, 0));
        createBookingDto.setEnd(LocalDateTime.of(2025, 1, 12, 12, 0));
        createBookingDto.setItemId(1L);
        createBookingDto.setStatus("WAITING");

        JsonContent<CreateBookingDto> result = json.write(createBookingDto);

        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo("2025-01-12T10:00:00");
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo("2025-01-12T12:00:00");
        assertThat(result).extractingJsonPathNumberValue("$.itemId").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.status").isEqualTo("WAITING");
    }

    @Test
    void testCreateBookingDtoDeserialization() throws Exception {
        String jsonContent = "{\"start\": \"2025-01-12T10:00:00\", \"end\": \"2025-01-12T12:00:00\", \"itemId\": 1, \"status\": \"APPROVED\"}";

        CreateBookingDto result = json.parse(jsonContent).getObject();

        assertThat(result.getStart()).isEqualTo(LocalDateTime.of(2025, 1, 12, 10, 0));
        assertThat(result.getEnd()).isEqualTo(LocalDateTime.of(2025, 1, 12, 12, 0));
        assertThat(result.getItemId()).isEqualTo(1L);
        assertThat(result.getStatus()).isEqualTo("APPROVED");
    }
}
