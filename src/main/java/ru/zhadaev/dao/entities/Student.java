package ru.zhadaev.dao.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(schema = "school", name = "students")
@Data
public class Student {
    @Id
    private Integer id;

    @ManyToOne
    @Column(name = "group_id")
    private Group group;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @ManyToMany
    @JoinTable(name = "students_courses",
    joinColumns = @JoinColumn(name = "student_id"),
    inverseJoinColumns = @JoinColumn(name = "course_id"))
    private List<Course> courses;
}
