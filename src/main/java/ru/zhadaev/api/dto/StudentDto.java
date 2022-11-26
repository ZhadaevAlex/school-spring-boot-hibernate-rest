package ru.zhadaev.api.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;
import java.util.UUID;

@Data
public class StudentDto {
    private UUID id;

    @NotBlank(message = "The student's first name cannot be empty")
    @Size(min = 2, message = "The student's first name must consist of at least two characters")
    private String firstName;

    @NotBlank(message = "The student's last name cannot be empty")
    @Size(min = 2, message = "The student's last name must consist of at least two characters")
    private String lastName;

    private GroupDto group;
    private Set<CourseDto> courses;
}

