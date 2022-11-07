package ru.zhadaev.dao.entities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(schema = "school", name = "courses")
@Data
public class Course {
    @Id
    @SequenceGenerator(
            schema = "school",
            name = "course_seq",
            sequenceName = "courses_course_id_sequence",
            initialValue = 11,
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "course_seq")
    @Column(name = "course_id")
    private Integer id;

    @Column(name = "course_name")
    private String name;

    @Column(name = "course_description")
    private String description;
}
