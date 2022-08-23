package ru.zhadaev.dao.entities;

import lombok.Data;

import java.util.List;

@Data
public class Student {
    private Integer id;
    private Group group;
    private String firstName;
    private String lastName;
    private List<Course> courses;
}
