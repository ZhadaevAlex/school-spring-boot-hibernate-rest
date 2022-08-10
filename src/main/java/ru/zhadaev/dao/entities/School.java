package ru.zhadaev.dao.entities;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class School {
    private List<Group> groups = new ArrayList<>();
    private List<Course> courses = new ArrayList<>();
    private List<Student> students = new ArrayList<>();
}
