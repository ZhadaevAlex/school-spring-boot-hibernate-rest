package ru.zhadaev.api.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Data
public class CourseDto {
    private UUID id;

    @NotBlank(message = "The course name must consist of at least one character")
    private String name;

    private String description;
}
