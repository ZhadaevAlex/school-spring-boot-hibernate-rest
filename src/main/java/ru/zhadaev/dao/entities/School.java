package ru.zhadaev.dao.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class School {
    private List<Group> groups = new ArrayList<>();
    private List<Course> courses = new ArrayList<>();
    private List<Student> students = new ArrayList<>();
}
