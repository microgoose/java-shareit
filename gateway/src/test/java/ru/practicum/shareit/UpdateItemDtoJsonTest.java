package ru.practicum.shareit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.UpdateItemDto;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class UpdateItemDtoJsonTest {

    @Autowired
    private JacksonTester<UpdateItemDto> json;

    @Test
    void testUpdateItemDtoSerialization() throws Exception {
        UpdateItemDto updateItemDto = new UpdateItemDto(
                "Updated item name",
                "Updated item description",
                false
        );

        JsonContent<UpdateItemDto> result = json.write(updateItemDto);

        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("Updated item name");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("Updated item description");
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(false);
    }

    @Test
    void testUpdateItemDtoDeserialization() throws Exception {
        String jsonContent = "{\"name\": \"Updated item name\",\"description\": \"Updated item description\",\"available\": false}";

        UpdateItemDto result = json.parse(jsonContent).getObject();

        assertThat(result.getName()).isEqualTo("Updated item name");
        assertThat(result.getDescription()).isEqualTo("Updated item description");
        assertThat(result.getAvailable()).isEqualTo(false);
    }
}
