package ru.practicum.shareit.features.item.mapper;

import ru.practicum.shareit.features.item.dto.CommentDto;
import ru.practicum.shareit.features.item.model.Comment;

public class CommentMapper {

    public static CommentDto toDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .authorName(comment.getAuthor().getName())
                .created(comment.getCreated())
                .build();
    }
}
