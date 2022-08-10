package ru.zhadaev.dao.entities;

import java.util.List;
import lombok.Data;

@Data
public class Student {
    private Integer id;
    private Group group;
    private String firstName;
    private String lastName;
    private List<Course> courses;
}
