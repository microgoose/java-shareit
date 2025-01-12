package ru.practicum.shareit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class ItemRequestDtoJsonTest {

    @Autowired
    private JacksonTester<ItemRequestDto> json;

    @Test
    void testItemRequestDtoSerialization() throws Exception {
        LocalDateTime createdTime = LocalDateTime.of(2025, 1, 10, 12, 0);
        ItemRequestDto itemRequestDto = ItemRequestDto.builder()
                .description("Request for an item")
                .created(createdTime)
                .build();
        JsonContent<ItemRequestDto> result = json.write(itemRequestDto);

        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("Request for an item");
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo("2025-01-10T12:00:00");
    }

    @Test
    void testItemRequestDtoDeserialization() throws Exception {
        String jsonContent = "{\"description\": \"Request for an item\", \"created\": \"2025-01-10T12:00:00\"}";

        ItemRequestDto result = json.parse(jsonContent).getObject();

        assertThat(result.getDescription()).isEqualTo("Request for an item");
        assertThat(result.getCreated()).isEqualTo(LocalDateTime.of(2025, 1, 10, 12, 0));
    }
}
