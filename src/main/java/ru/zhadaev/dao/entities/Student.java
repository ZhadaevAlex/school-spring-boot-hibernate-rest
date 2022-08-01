package ru.zhadaev.dao.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Student {
    private Integer id;
    private Group group;
    private String firstName;
    private String lastName;
    private List<Course> courses;
}
