package ru.zhadaev.dto;

import lombok.Data;

import java.util.Set;

@Data
public class StudentDto {
    private Integer id;
    private String firstName;
    private String lastName;
    private GroupDto group;
    private Set<CourseDto> courses;
}

