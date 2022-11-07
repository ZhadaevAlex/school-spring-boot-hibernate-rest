package ru.zhadaev.dao.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "students", schema = "school")
@Data
public class Student {
    @Id
    @SequenceGenerator(
            schema = "school",
            name = "student_seq",
            sequenceName = "students_student_id_sequence",
            initialValue = 201,
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "student_seq")
    @Column(name = "student_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "students_courses", schema = "school",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private Set<Course> courses;
}
