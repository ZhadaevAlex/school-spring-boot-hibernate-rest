package ru.zhadaev;

import java.util.Set;

public class Student {
    private Integer id;
    private Group group;
    private final String firstName;
    private final String lastName;
    private Set<Course> courses;

    public Student(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Group getGroup() {
        return group;
    }

    public void setCourse(Set<Course> courses) {
        this.courses = courses;
    }

    public Set<Course> getCourse() {
        return courses;
    }
}
