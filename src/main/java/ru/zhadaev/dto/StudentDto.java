package ru.zhadaev.dto;

import lombok.Data;
import ru.zhadaev.dao.entities.Course;
import ru.zhadaev.dao.entities.Group;

import java.util.Set;

@Data
public class StudentDto {
    private Integer id;
    private Group group;
    private String firstName;
    private String lastName;
    private Set<Course> courses;
}

