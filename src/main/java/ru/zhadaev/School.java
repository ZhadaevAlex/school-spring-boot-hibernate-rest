package ru.zhadaev;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class School {
    private Set<Group> groups = new HashSet<>();
    private List<Course> courses = new ArrayList<>();
    private List<Student> students = new ArrayList<>();

    public void setGroups(Set<Group> groups) {
        this.groups = groups;
    }

    public Set<Group> getGroups() {
        return groups;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public List<Student> getStudents() {
        return students;
    }
}
