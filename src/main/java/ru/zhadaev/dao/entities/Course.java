package ru.zhadaev.dao.entities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(schema = "school", name = "courses")
@Data
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    private Integer id;

    @Column(name = "course_name")
    private String name;

    @Column(name = "course_description")
    private String description;
}
