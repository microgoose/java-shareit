package ru.practicum.shareit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.CommentDto;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class CommentDtoJsonTest {

    @Autowired
    private JacksonTester<CommentDto> json;

    @Test
    void testCommentDtoSerialization() throws Exception {
        CommentDto commentDto = new CommentDto("This is a comment.");
        JsonContent<CommentDto> result = json.write(commentDto);
        assertThat(result).extractingJsonPathStringValue("$.text").isEqualTo("This is a comment.");
    }

    @Test
    void testCommentDtoDeserialization() throws Exception {
        String jsonContent = "{\"text\": \"This is a comment.\"}";


        CommentDto result = json.parse(jsonContent).getObject();
        assertThat(result.getText()).isEqualTo("This is a comment.");
    }
}
