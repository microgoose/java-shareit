package ru.practicum.shareit.features.item.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateItemDto {
    @NotBlank(message = "Наименование обязательно")
    private String name;

    @NotBlank(message = "Описание обязательно")
    private String description;
    private Boolean available;
    private Long requestId;
}
