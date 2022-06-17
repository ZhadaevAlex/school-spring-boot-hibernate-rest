package ru.zhadaev.dao.entities;

import java.util.ArrayList;
import java.util.List;

public class School {
    private List<Group> groups = new ArrayList<>();
    private List<Course> courses = new ArrayList<>();
    private List<Student> students = new ArrayList<>();

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }
}
