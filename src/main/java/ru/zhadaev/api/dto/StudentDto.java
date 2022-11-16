package ru.zhadaev.api.dto;

import lombok.Data;
import ru.zhadaev.api.validation.Marker;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;
import java.util.UUID;

@Data
public class StudentDto {
    private UUID id;

    @NotBlank(groups = Marker.OnPostPut.class,
            message = "The student's first name cannot be empty")
    @Size(groups = Marker.OnPostPut.class,
            min = 2,
    message = "The student's first name must consist of at least two characters")
    private String firstName;

    @NotBlank(groups = Marker.OnPostPut.class,
            message = "The student's last name cannot be empty")
    @Size(groups = Marker.OnPostPut.class,
            min = 2,
            message = "The student's last name must consist of at least two characters")
    private String lastName;

    private GroupDto group;
    private Set<CourseDto> courses;
}

