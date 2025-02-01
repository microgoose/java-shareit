package ru.practicum.shareit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.CreateItemDto;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class CreateItemDtoJsonTest {

    @Autowired
    private JacksonTester<CreateItemDto> json;

    @Test
    void testCreateItemDtoSerialization() throws Exception {
        CreateItemDto createItemDto = new CreateItemDto(
                "Item name",
                "Item description",
                true,
                123L
        );

        JsonContent<CreateItemDto> result = json.write(createItemDto);

        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("Item name");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("Item description");
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(true);
        assertThat(result).extractingJsonPathNumberValue("$.requestId").isEqualTo(123);
    }

    @Test
    void testCreateItemDtoDeserialization() throws Exception {
        String jsonContent = "{\"name\": \"Item name\", \"description\": \"Item description\", \"available\": true, \"requestId\": 123}";

        CreateItemDto result = json.parse(jsonContent).getObject();

        assertThat(result.getName()).isEqualTo("Item name");
        assertThat(result.getDescription()).isEqualTo("Item description");
        assertThat(result.getAvailable()).isEqualTo(true);
        assertThat(result.getRequestId()).isEqualTo(123L);
    }
}
