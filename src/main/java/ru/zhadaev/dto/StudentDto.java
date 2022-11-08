package ru.zhadaev.dto;

import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Data
public class StudentDto {
    private UUID id;
    private String firstName;
    private String lastName;
    private GroupDto group;
    private Set<CourseDto> courses;
}

