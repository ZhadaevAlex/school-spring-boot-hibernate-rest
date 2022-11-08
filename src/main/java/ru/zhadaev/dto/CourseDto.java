package ru.zhadaev.dto;

import lombok.Data;
import ru.zhadaev.validation.Marker;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Data
public class CourseDto {
    private UUID id;

    @NotBlank(groups = Marker.OnPostPut.class,
            message = "The course name must consist of at least one character")
    private String name;
    private String description;
}
